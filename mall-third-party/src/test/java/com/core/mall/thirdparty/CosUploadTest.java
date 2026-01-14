package com.core.mall.thirdparty;

import com.core.mall.thirdparty.service.CosService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 腾讯云COS文件上传测试类
 */
@Slf4j
@SpringBootTest
public class CosUploadTest {

    @Autowired
    private CosService cosService;

    /**
     * 测试上传图片到腾讯云COS
     * <p>
     * 使用说明:
     * 1. 将下面的 LOCAL_IMAGE_PATH 替换为你本地图片的实际路径
     * 2. 确保 Nacos 配置中心已正确配置 COS 参数（cos.yml）
     * 3. 运行此测试方法
     * 4. 查看控制台输出的图片访问URL
     */
    @Test
    public void testUploadImage() throws IOException {
        // TODO: 替换为你本地图片的实际路径
        String LOCAL_IMAGE_PATH = "/Users/weiren/Downloads/微信图片_20251218094354_28_373.jpg";

        // 读取本地图片文件
        File imageFile = new File(LOCAL_IMAGE_PATH);

        if (!imageFile.exists()) {
            log.error("图片文件不存在: {}", LOCAL_IMAGE_PATH);
            throw new IllegalArgumentException("图片文件不存在，请检查路径: " + LOCAL_IMAGE_PATH);
        }

        log.info("开始上传图片: {}", imageFile.getName());
        log.info("文件大小: {} KB", imageFile.length() / 1024);

        // 将File转换为MultipartFile
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        MultipartFile multipartFile = new MockMultipartFile(
                imageFile.getName(),           // 文件名
                imageFile.getName(),           // 原始文件名
                getContentType(imageFile),     // 内容类型
                fileInputStream                // 文件输入流
        );

        // 调用上传服务
        String imageUrl = cosService.uploadFile(multipartFile);

        // 输出上传结果
        log.info("========================================");
        log.info("图片上传成功！");
        log.info("图片访问URL: {}", imageUrl);
        log.info("========================================");

        // 关闭流
        fileInputStream.close();

        // 验证URL不为空
        assert imageUrl != null && !imageUrl.isEmpty();
    }

    /**
     * 根据文件扩展名获取Content-Type
     *
     * @param file 文件
     * @return Content-Type
     */
    private String getContentType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else {
            return "application/octet-stream";
        }
    }
}