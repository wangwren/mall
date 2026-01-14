package com.core.mall.thirdparty;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.core.mall.thirdparty.dto.GetUploadSignDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

/**
 * 前端直传测试类
 * 模拟前端获取签名并直接上传到COS的完整流程
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CosPresignedUploadTest {

    // 后端服务地址
    private static final String BACKEND_URL = "http://localhost:8090";

    /**
     * 测试前端直传流程
     * <p>
     * 完整流程：
     * 1. 前端调用后端接口获取预签名URL
     * 2. 前端使用预签名URL直接上传文件到COS（不经过后端）
     * 3. 上传成功后，可直接访问返回的accessUrl
     * <p>
     * 使用说明：
     * 1. 修改 LOCAL_IMAGE_PATH 为你本地图片路径
     * 2. 确保 mall-third-party 服务已启动（端口8400）
     * 3. 确保 Nacos 配置正确
     * 4. 运行此测试方法
     */
    @Test
    public void testPresignedUpload() throws IOException {
        // TODO: 替换为你本地图片的实际路径
        String LOCAL_IMAGE_PATH = "/Users/weiren/Downloads/微信图片_20251218094354_28_373.jpg";

        // 读取本地图片文件
        File imageFile = new File(LOCAL_IMAGE_PATH);
        if (!imageFile.exists()) {
            log.error("图片文件不存在: {}", LOCAL_IMAGE_PATH);
            throw new IllegalArgumentException("图片文件不存在，请检查路径: " + LOCAL_IMAGE_PATH);
        }

        log.info("========================================");
        log.info("开始测试前端直传流程");
        log.info("文件名: {}", imageFile.getName());
        log.info("文件大小: {} KB", imageFile.length() / 1024);
        log.info("========================================");

        // ========== 步骤1：调用后端接口获取预签名URL ==========
        log.info("\n【步骤1】调用后端获取上传签名");

        GetUploadSignDTO signRequest = new GetUploadSignDTO();
        signRequest.setFileName(imageFile.getName());
        signRequest.setContentType(getContentType(imageFile));
        signRequest.setFileSize(imageFile.length());
        signRequest.setCustomDir("test"); // 可选：指定上传目录

        String requestBody = JSONUtil.toJsonStr(signRequest);
        log.info("请求参数: {}", requestBody);

        HttpResponse signResponse = HttpRequest.post(BACKEND_URL + "/thirdparty/cos/getUploadSign")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute();

        String signResponseBody = signResponse.body();
        log.info("响应状态: {}", signResponse.getStatus());
        log.info("响应内容: {}", signResponseBody);

        if (signResponse.getStatus() != 200) {
            throw new RuntimeException("获取签名失败: " + signResponseBody);
        }

        // 解析响应
        JSONObject responseJson = JSONUtil.parseObj(signResponseBody);
        if (responseJson.getInt("code") != 0) {
            throw new RuntimeException("获取签名失败: " + responseJson.getStr("msg"));
        }

        JSONObject signData = responseJson.getJSONObject("data");
        String uploadUrl = signData.getStr("uploadUrl");
        String accessUrl = signData.getStr("accessUrl");
        String objectKey = signData.getStr("objectKey");

        log.info("✓ 获取签名成功");
        log.info("  - 上传URL: {}", uploadUrl);
        log.info("  - 对象键: {}", objectKey);
        log.info("  - 访问URL: {}", accessUrl);

        // ========== 步骤2：使用预签名URL直接上传到COS ==========
        log.info("\n【步骤2】直接上传文件到腾讯云COS（不经过后端）");

        byte[] fileBytes = Files.readAllBytes(imageFile.toPath());

        HttpResponse uploadResponse = HttpRequest.put(uploadUrl)
                .header("Content-Type", signRequest.getContentType())
                .body(fileBytes)
                .execute();

        log.info("上传状态: {}", uploadResponse.getStatus());
        log.info("响应头: {}", uploadResponse.headers());

        if (uploadResponse.getStatus() != 200) {
            throw new RuntimeException("上传失败，状态码: " + uploadResponse.getStatus());
        }

        log.info("✓ 文件上传成功！");

        // ========== 步骤3：输出结果 ==========
        log.info("\n========================================");
        log.info("前端直传测试成功！");
        log.info("文件访问URL: {}", accessUrl);
        log.info("========================================");
        log.info("\n提示：你可以在浏览器中访问上面的URL查看图片");

        // 验证
        assert uploadResponse.getStatus() == 200;
    }

    /**
     * 测试获取签名接口（仅获取签名，不上传）
     */
    @Test
    public void testGetUploadSignOnly() {
        log.info("========================================");
        log.info("测试获取上传签名接口");
        log.info("========================================");

        GetUploadSignDTO request = new GetUploadSignDTO();
        request.setFileName("test-image.jpg");
        request.setContentType("image/jpeg");
        request.setFileSize(102400L); // 100KB

        String requestBody = JSONUtil.toJsonStr(request);
        log.info("请求参数: {}", requestBody);

        HttpResponse response = HttpRequest.post(BACKEND_URL + "/thirdparty/cos/getUploadSign")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute();

        log.info("响应状态: {}", response.getStatus());
        log.info("响应内容: {}", response.body());

        JSONObject responseJson = JSONUtil.parseObj(response.body());
        if (responseJson.getInt("code") == 0) {
            JSONObject data = responseJson.getJSONObject("data");
            log.info("\n签名信息:");
            log.info("  - 上传URL: {}", data.getStr("uploadUrl"));
            log.info("  - 对象键: {}", data.getStr("objectKey"));
            log.info("  - 访问URL: {}", data.getStr("accessUrl"));
            log.info("  - 过期时间: {}", data.getStr("expiration"));
            log.info("  - 最大大小: {} MB", data.getLong("maxSize") / 1024 / 1024);
        }

        assert response.getStatus() == 200;
    }

    /**
     * 测试文件大小限制
     */
    @Test
    public void testFileSizeLimit() {
        log.info("========================================");
        log.info("测试文件大小限制（应该失败）");
        log.info("========================================");

        GetUploadSignDTO request = new GetUploadSignDTO();
        request.setFileName("large-file.jpg");
        request.setContentType("image/jpeg");
        request.setFileSize(20 * 1024 * 1024L); // 20MB，超过10MB限制

        String requestBody = JSONUtil.toJsonStr(request);

        HttpResponse response = HttpRequest.post(BACKEND_URL + "/thirdparty/cos/getUploadSign")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute();

        log.info("响应状态: {}", response.getStatus());
        log.info("响应内容: {}", response.body());

        JSONObject responseJson = JSONUtil.parseObj(response.body());
        log.info("预期失败，错误信息: {}", responseJson.getStr("msg"));

        // 验证应该返回错误
        assert responseJson.getInt("code") != 0;
    }

    /**
     * 测试文件类型限制
     */
    @Test
    public void testFileTypeLimit() {
        log.info("========================================");
        log.info("测试文件类型限制（应该失败）");
        log.info("========================================");

        GetUploadSignDTO request = new GetUploadSignDTO();
        request.setFileName("document.pdf");
        request.setContentType("application/pdf"); // 非图片类型
        request.setFileSize(102400L);

        String requestBody = JSONUtil.toJsonStr(request);

        HttpResponse response = HttpRequest.post(BACKEND_URL + "/thirdparty/cos/getUploadSign")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute();

        log.info("响应状态: {}", response.getStatus());
        log.info("响应内容: {}", response.body());

        JSONObject responseJson = JSONUtil.parseObj(response.body());
        log.info("预期失败，错误信息: {}", responseJson.getStr("msg"));

        // 验证应该返回错误
        assert responseJson.getInt("code") != 0;
    }

    /**
     * 根据文件扩展名获取Content-Type
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