package com.core.mall.thirdparty.controller;

import com.core.mall.common.utils.Result;
import com.core.mall.thirdparty.service.CosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问URL
     */
    @PostMapping("/upload")
    @Operation(summary = "上传文件")
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
}
