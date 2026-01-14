package com.core.mall.thirdparty.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * COS上传签名DTO
 * 用于前端直传到腾讯云COS
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "COS上传签名信息")
public class UploadSignDTO implements Serializable {

    @Schema(description = "预签名上传URL（前端直接PUT到这个URL）")
    private String uploadUrl;

    @Schema(description = "文件在COS中的对象键（路径）")
    private String objectKey;

    @Schema(description = "文件访问URL（上传成功后可直接访问）")
    private String accessUrl;

    @Schema(description = "签名过期时间")
    private Date expiration;

    @Schema(description = "允许的Content-Type")
    private String contentType;

    @Schema(description = "最大文件大小（字节）")
    private Long maxSize;
}