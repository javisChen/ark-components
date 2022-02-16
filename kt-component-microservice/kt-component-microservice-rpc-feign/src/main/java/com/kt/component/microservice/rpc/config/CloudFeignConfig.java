package com.kt.component.microservice.rpc.config;

import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashSet;

/**
 * Feign配置
 * @author jc
 */
@Data
@RefreshScope
public class CloudFeignConfig {

    /**
     * feign调用时需要透传的header
     */
    private HashSet<String> transmitHeaders;
}
