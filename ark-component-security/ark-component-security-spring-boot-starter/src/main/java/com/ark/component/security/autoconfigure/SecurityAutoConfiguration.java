package com.ark.component.security.autoconfigure;

import com.ark.component.security.autoconfigure.jwt.JWTConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * 安全组件自动注入
 */
@Slf4j
@Import(value = {
        SecurityConfiguration.class,
        JWTConfiguration.class
})
@EnableConfigurationProperties(value = SecurityProperties.class)
public class SecurityAutoConfiguration {

    public SecurityAutoConfiguration() {
        log.info("enable [ark-component-security-spring-boot-starter]");
    }
}