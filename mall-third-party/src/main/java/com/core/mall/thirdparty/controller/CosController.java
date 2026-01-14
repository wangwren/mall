package com.core.mall.thirdparty.controller;

import com.core.mall.common.utils.Result;
import com.core.mall.common.validator.ValidatorUtils;
import com.core.mall.thirdparty.dto.GetUploadSignDTO;
import com.core.mall.thirdparty.dto.UploadSignDTO;
import com.core.mall.thirdparty.service.CosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/thirdparty/cos")
@Tag(name = "腾讯云COS文件上传")
public class CosController {

    @Autowired
    private CosService cosService;

    /**
     * 上传文件（服务端直传方式）
     *
     * @param file 文件
     * @return 文件访问URL
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件（服务端直传）", description = "文件上传到后端，由后端转发到COS")
    public Result<Map<String, Object>> upload(
            @Parameter(description = "上传文件") @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new Result<Map<String, Object>>().error("文件不能为空");
            }

            String url = cosService.uploadFile(file);

            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("filename", file.getOriginalFilename());
            data.put("size", file.getSize());

            log.info("文件上传成功: {}", url);

            return new Result<Map<String, Object>>().ok(data);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return new Result<Map<String, Object>>().error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取上传签名（前端直传方式）
     *
     * 使用流程：
     * 1. 前端调用此接口获取预签名URL
     * 2. 前端使用返回的uploadUrl直接PUT文件到COS
     * 3. 上传成功后，可使用accessUrl访问文件
     *
     * 示例：
     * fetch(uploadUrl, {
     *   method: 'PUT',
     *   headers: { 'Content-Type': contentType },
     *   body: file
     * })
     *
     * @param request 上传签名请求参数
     * @return 预签名URL和文件信息
     */
    @PostMapping("/getUploadSign")
    @Operation(summary = "获取上传签名（前端直传）", description = "前端获取签名后直接上传到COS，不经过后端服务器")
    public Result<UploadSignDTO> getUploadSign(@RequestBody GetUploadSignDTO request) {
        try {
            // 参数校验
            ValidatorUtils.validateEntity(request);

            // 生成上传签名
            UploadSignDTO signDTO = cosService.generateUploadSign(request);

            log.info("生成上传签名成功: {}", signDTO.getObjectKey());

            return new Result<UploadSignDTO>().ok(signDTO);
        } catch (IllegalArgumentException e) {
            log.warn("获取上传签名失败: {}", e.getMessage());
            return new Result<UploadSignDTO>().error(e.getMessage());
        } catch (Exception e) {
            log.error("获取上传签名失败", e);
            return new Result<UploadSignDTO>().error("获取上传签名失败: " + e.getMessage());
        }
    }
}
