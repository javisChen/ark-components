package com.kt.component.microservice.rpc.autoconfigure;

import com.kt.component.microservice.rpc.config.CloudFeignConfig;
import com.kt.component.microservice.rpc.exception.FeignCommonErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;

@Slf4j
public class CloudFeignAutoConfiguration {

    @Value("${kt.cloud.feign.transmit-headers:}")
    private HashSet<String> transmitHeaders;

    public CloudFeignAutoConfiguration() {
        log.info("enable [kt-component-microservice-rpc-feign]");
    }

    @Bean
    public CloudFeignConfig cloudFeignConfig() {
        CloudFeignConfig cloudFeignConfig = new CloudFeignConfig();
        cloudFeignConfig.setTransmitHeaders(transmitHeaders);
        return cloudFeignConfig;
    }

    @Bean
    public Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

    @ConditionalOnMissingBean(ErrorDecoder.class)
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignCommonErrorDecoder();
    }



}
