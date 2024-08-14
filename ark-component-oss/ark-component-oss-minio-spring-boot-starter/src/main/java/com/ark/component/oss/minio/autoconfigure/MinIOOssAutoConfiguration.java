package com.ark.component.oss.minio.autoconfigure;

import com.ark.component.oss.minio.MinIOOssProperties;
import com.ark.component.oss.minio.MinIoOssObjectStorageService;
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
@EnableConfigurationProperties(value = {MinIOOssProperties.class})
public class MinIOOssAutoConfiguration {

    public MinIOOssAutoConfiguration() {
        log.info("enable [ark-component-oss-minio-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnProperty(value = "ark.component.oss.minio.enabled", havingValue = "true")
    public MinIoOssObjectStorageService minIoOssObjectStorageService(MinIOOssProperties minIoOssProperties) {
        return new MinIoOssObjectStorageService(minIoOssProperties);
    }

}
