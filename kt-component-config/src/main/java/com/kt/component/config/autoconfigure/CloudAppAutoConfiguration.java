package com.kt.component.config.autoconfigure;

import com.kt.component.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class CloudAppAutoConfiguration implements InitializingBean {

    @Value("${spring.application.name:}")
    private String applicationName;

    public CloudAppAutoConfiguration() {
        log.info("enable [kt-component-config]");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AppConfig.setServiceName(applicationName);
    }
}
