package com.core.mall.thirdparty.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.core.mall.thirdparty.config.CosProperties;
import com.core.mall.thirdparty.dto.GetUploadSignDTO;
import com.core.mall.thirdparty.dto.UploadSignDTO;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.Headers;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

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

    /**
     * 生成预签名上传URL（用于前端直传）
     *
     * 流程说明：
     * 1. 前端调用此接口获取预签名URL
     * 2. 前端使用预签名URL直接上传文件到COS（不经过后端）
     * 3. 上传完成后，前端可选择性通知后端记录文件信息
     *
     * @param request 上传签名请求参数
     * @return 包含预签名URL和文件信息的DTO
     */
    public UploadSignDTO generateUploadSign(GetUploadSignDTO request) {
        // 验证文件大小（默认最大10MB）
        long maxSize = 10 * 1024 * 1024L;
        if (request.getFileSize() > maxSize) {
            throw new IllegalArgumentException("文件大小不能超过10MB");
        }

        // 验证文件类型（仅允许图片）
        String contentType = request.getContentType().toLowerCase();
        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("仅支持上传图片文件");
        }

        // 生成对象键（文件路径）
        String objectKey = generateObjectKey(request.getFileName(), request.getCustomDir());

        // 设置签名过期时间（15分钟）
        Date expiration = new Date(System.currentTimeMillis() + 15 * 60 * 1000);

        // 创建预签名URL请求
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                cosProperties.getBucketName(),
                objectKey,
                HttpMethodName.PUT
        );
        urlRequest.setExpiration(expiration);

        // 设置请求头，限制Content-Type
        urlRequest.putCustomRequestHeader(Headers.CONTENT_TYPE, request.getContentType());

        // 生成预签名URL
        URL presignedUrl = cosClient.generatePresignedUrl(urlRequest);

        // 构建文件访问URL
        String accessUrl = cosProperties.getCdnUrl() + "/" + objectKey;

        log.info("生成上传签名成功: objectKey={}, expiration={}", objectKey, expiration);

        // 返回签名信息
        UploadSignDTO result = new UploadSignDTO();
        result.setUploadUrl(presignedUrl.toString());
        result.setObjectKey(objectKey);
        result.setAccessUrl(accessUrl);
        result.setExpiration(expiration);
        result.setContentType(request.getContentType());
        result.setMaxSize(maxSize);

        return result;
    }

    /**
     * 生成对象键（文件存储路径）
     *
     * @param originalFilename 原始文件名
     * @param customDir 自定义目录（可选）
     * @return 对象键
     */
    private String generateObjectKey(String originalFilename, String customDir) {
        // 提取文件扩展名
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成日期目录
        String dateFolder = DateUtil.format(DateUtil.date(), "yyyy/MM/dd");

        // 生成UUID文件名
        String uuid = IdUtil.simpleUUID();

        // 构建完整路径
        if (StrUtil.isNotBlank(customDir)) {
            // 自定义目录/日期/UUID.扩展名
            return customDir + "/" + dateFolder + "/" + uuid + extension;
        } else {
            // 日期/UUID.扩展名
            return dateFolder + "/" + uuid + extension;
        }
    }
}
