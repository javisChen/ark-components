package com.ark.component.security.autoconfigure;

import com.ark.component.security.base.token.AccessTokenProperties;
import com.ark.component.security.core.token.extractor.DefaultTokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 安全组件自动注入
 */
@Slf4j
public class SecurityAutoConfiguration {

    public SecurityAutoConfiguration() {
        log.info("enable [ark-component-security-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessTokenProperties accessTokenProperties() {
        return new AccessTokenProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultTokenExtractor accessTokenExtractor(AccessTokenProperties accessTokenProperties) {
        return new DefaultTokenExtractor(accessTokenProperties);
    }

}