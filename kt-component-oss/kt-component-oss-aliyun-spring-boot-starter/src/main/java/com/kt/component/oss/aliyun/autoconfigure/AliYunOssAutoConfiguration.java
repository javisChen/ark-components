package com.kt.component.oss.aliyun.autoconfigure;

import com.kt.component.oss.aliyun.AliYunObjectStorageService;
import com.kt.component.oss.aliyun.AliYunOssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
public class AliYunOssAutoConfiguration {

    public AliYunOssAutoConfiguration() {
        log.info("enable [kt-component-oss-aliyun-spring-boot-starter]");
    }

    @Bean
    public AliYunObjectStorageService aliYunObjectStorageService(AliYunOssProperties aliYunOssProperties) {
        return new AliYunObjectStorageService(aliYunOssProperties);
    }

}
