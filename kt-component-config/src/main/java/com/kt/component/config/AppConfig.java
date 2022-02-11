package com.kt.component.config;

import org.springframework.context.annotation.Configuration;

/**
 * 应用通用配置
 * @author jc
 */
@Configuration
public class AppConfig {

    private static String serviceName;

    public static void setServiceName(String serviceName) {
        AppConfig.serviceName = serviceName;
    }

    public static String getServiceName() {
        return serviceName;
    }
}
