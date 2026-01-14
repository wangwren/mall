package com.core.mall.thirdparty.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云COS配置
 */
@Slf4j
@Configuration
public class CosConfig {

    @Autowired
    private CosProperties cosProperties;

    @Bean
    public COSClient cosClient() {
        // 1. 初始化用户身份信息
        COSCredentials cred = new BasicCOSCredentials(
                cosProperties.getSecretId(),
                cosProperties.getSecretKey()
        );

        // 2. 设置bucket的地域
        Region region = new Region(cosProperties.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);

        // 3. 生成COS客户端
        COSClient cosClient = new COSClient(cred, clientConfig);

        log.info("腾讯云COS客户端初始化成功，Region: {}, Bucket: {}",
                cosProperties.getRegion(),
                cosProperties.getBucketName());

        return cosClient;
    }
}
