package com.ark.component.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 应用通用配置
 * @author jc
 */
public class ArkWebConfig implements WebMvcConfigurer {

    @Autowired
    private List<HandlerInterceptor> handlerInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        handlerInterceptors.forEach(registry::addInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath*:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath*:/META-INF/resources/webjars/");
    }

    private static String serviceName;

    @Value("${spring.application.name}")
    public void setServiceName(String serviceName) {
        ArkWebConfig.serviceName = serviceName;
    }

    public static String getServiceName() {
        return serviceName;
    }

//    public void setHandlerInterceptors(List<HandlerInterceptor> handlerInterceptors) {
//        this.handlerInterceptors = handlerInterceptors;
//    }
}
