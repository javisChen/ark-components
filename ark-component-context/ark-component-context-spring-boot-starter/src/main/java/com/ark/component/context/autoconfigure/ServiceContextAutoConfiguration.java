package com.ark.component.context.autoconfigure;

import com.ark.component.context.core.interceptor.ServiceContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
public class ServiceContextAutoConfiguration implements WebMvcConfigurer {

    public ServiceContextAutoConfiguration() {
        log.info("enable [ark-component-context-spring-boot-starter]");
    }

    @Bean
    public ServiceContextInterceptor serviceContextInterceptor() {
        return new ServiceContextInterceptor();
    }
}