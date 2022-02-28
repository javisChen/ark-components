package com.kt.component.oss.minio.autoconfigure;

import com.kt.component.oss.minio.MinIoOssObjectStorageService;
import com.kt.component.oss.minio.MinIoOssProperties;
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
@EnableConfigurationProperties(value = {MinIoOssProperties.class})
public class MinIoOssAutoConfiguration {

    public MinIoOssAutoConfiguration() {
        log.info("enable [kt-component-oss-minio-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnProperty(value = "kt.component.oss.minio", havingValue = "true")
    public MinIoOssObjectStorageService minIoOssObjectStorageService(MinIoOssProperties minIoOssProperties) {
        return new MinIoOssObjectStorageService(minIoOssProperties);
    }

}
