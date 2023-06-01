package com.ark.component.microservice.rpc.autoconfigure;

import com.ark.component.microservice.rpc.config.CloudFeignProperties;
import com.ark.component.microservice.rpc.exception.FeignCommonErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.HashSet;

@Slf4j
@EnableConfigurationProperties(CloudFeignProperties.class)
public class CloudFeignAutoConfiguration {

    public CloudFeignAutoConfiguration() {
        log.info("enable [ark-component-microservice-rpc-feign]");
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
