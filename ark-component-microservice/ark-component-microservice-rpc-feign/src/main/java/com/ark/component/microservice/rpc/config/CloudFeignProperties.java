package com.ark.component.microservice.rpc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;

/**
 * Feign配置
 * @author jc
 */
@ConfigurationProperties(value = "ark.cloud.feign")
@Data
@RefreshScope
public class CloudFeignProperties {

    /**
     * feign调用时需要透传的header
     */
    private HashSet<String> transmitHeaders;
}
