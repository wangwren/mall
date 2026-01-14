package com.core.mall.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 腾讯云COS配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "tencent.cos")
public class CosProperties {

    /**
     * SecretId
     */
    private String secretId;

    /**
     * SecretKey
     */
    private String secretKey;

    /**
     * 地域 (如: ap-guangzhou)
     */
    private String region;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 访问域名 (如: https://xxx.cos.ap-guangzhou.myqcloud.com)
     */
    private String cdnUrl;
}
