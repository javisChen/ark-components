package com.kt.component.microservice.rpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

/**
 * Feign配置
 * @author jc
 */
@Configuration
@ConfigurationProperties(value = "kt.cloud.feign")
@Data
@RefreshScope
public class CloudFeignConfig {

    /**
     * feign调用时需要透传的header
     */
    private HashSet<String> transmitHeaders;
}
