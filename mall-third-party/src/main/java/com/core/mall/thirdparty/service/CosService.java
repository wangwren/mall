package com.core.mall.thirdparty.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.core.mall.thirdparty.config.CosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 腾讯云COS文件上传服务
 */
@Slf4j
@Service
public class CosService {

    @Autowired
    private COSClient cosClient;

    @Autowired
    private CosProperties cosProperties;

    /**
     * 上传文件到腾讯云COS
     *
     * @param file 文件
     * @return 文件访问URL
     * @throws IOException IO异常
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 生成唯一文件名: 日期/UUID.扩展名
        String dateFolder = DateUtil.format(DateUtil.date(), "yyyy/MM/dd");
        String uuid = IdUtil.simpleUUID();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectKey = dateFolder + "/" + uuid + extension;

        // 获取文件输入流
        InputStream inputStream = file.getInputStream();

        // 设置元数据
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            // 创建上传请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    cosProperties.getBucketName(),
                    objectKey,
                    inputStream,
                    metadata
            );

            // 执行上传
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

            log.info("文件上传成功: {}, ETag: {}", objectKey, putObjectResult.getETag());

            // 返回文件访问URL
            return cosProperties.getCdnUrl() + "/" + objectKey;

        } finally {
            inputStream.close();
        }
    }

    /**
     * 删除文件
     *
     * @param objectKey 对象键（文件路径）
     */
    public void deleteFile(String objectKey) {
        cosClient.deleteObject(cosProperties.getBucketName(), objectKey);
        log.info("文件删除成功: {}", objectKey);
    }
}
