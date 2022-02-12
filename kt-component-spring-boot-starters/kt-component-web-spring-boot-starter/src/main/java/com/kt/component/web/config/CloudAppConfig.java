package com.kt.component.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 应用通用配置
 * @author jc
 */
public class CloudAppConfig implements WebMvcConfigurer {

    @Autowired
    private List<HandlerInterceptor> handlerInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        handlerInterceptors.forEach(registry::addInterceptor);
    }

    private static String serviceName;

    @Value("${spring.application.name}")
    public void setServiceName(String serviceName) {
        CloudAppConfig.serviceName = serviceName;
    }

    public static String getServiceName() {
        return serviceName;
    }

    public void setHandlerInterceptors(List<HandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors = handlerInterceptors;
    }
}
