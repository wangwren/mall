package com.core.mall.thirdparty.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 获取上传签名请求DTO
 */
@Data
@Schema(description = "获取上传签名请求")
public class GetUploadSignDTO implements Serializable {

    @NotBlank(message = "文件名不能为空")
    @Schema(description = "原始文件名（需包含扩展名）", example = "avatar.jpg")
    private String fileName;

    @NotBlank(message = "文件类型不能为空")
    @Schema(description = "文件MIME类型", example = "image/jpeg")
    private String contentType;

    @NotNull(message = "文件大小不能为空")
    @Schema(description = "文件大小（字节）", example = "102400")
    private Long fileSize;

    @Schema(description = "自定义上传目录（可选）", example = "avatar")
    private String customDir;
}