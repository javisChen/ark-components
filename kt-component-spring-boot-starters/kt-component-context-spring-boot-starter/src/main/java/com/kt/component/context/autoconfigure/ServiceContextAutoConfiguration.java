package com.kt.component.context.autoconfigure;

import com.kt.component.context.interceptor.ServiceContextInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
public class ServiceContextAutoConfiguration implements WebMvcConfigurer {

    public ServiceContextAutoConfiguration() {
        log.info("enable [kt-component-context-spring-boot-starter]");
    }

    @Bean
    public ServiceContextInterceptor serviceContextInterceptor() {
        return new ServiceContextInterceptor();
    }

}