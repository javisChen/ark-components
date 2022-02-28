package com.kt.component.oss.aliyun.autoconfigure;

import com.kt.component.oss.aliyun.AliYunObjectStorageService;
import com.kt.component.oss.aliyun.AliYunOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
@EnableConfigurationProperties(value = AliYunOssProperties.class)
public class AliYunOssAutoConfiguration {

    public AliYunOssAutoConfiguration() {
        log.info("enable [kt-component-oss-aliyun-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnProperty(value = "kt.component.oss.aliyun.enabled", havingValue = "true")
    public AliYunObjectStorageService aliYunObjectStorageService(AliYunOssProperties aliYunOssProperties) {
        return new AliYunObjectStorageService(aliYunOssProperties);
    }

}
