package com.kt.component.config.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class CloudAppAutoConfiguration implements InitializingBean {

    public CloudAppAutoConfiguration() {
        log.info("enable [kt-component-config]");
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
