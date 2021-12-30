package com.kt.component.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 应用通用配置
 * @author jc
 */
@Configuration
public class AppConfig {

    private static String serviceName;

    @Value("${spring.application.name:}")
    public void setInstanceId(String serviceName) {
        AppConfig.serviceName = serviceName;
    }

    public static String getServiceName() {
        return serviceName;
    }
}
