package com.ark.component.security.reactive.autoconfigure;

import com.ark.component.security.base.token.AccessTokenProperties;
import com.ark.component.security.base.token.extractor.AccessTokenExtractor;
import com.ark.component.security.reactive.token.ReactiveDefaultTokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 安全组件自动注入
 */
public class ReactiveSecurityAutoConfiguration {

    public ReactiveSecurityAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public AccessTokenProperties accessTokenProperties() {
        return new AccessTokenProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public ReactiveDefaultTokenExtractor accessTokenExtractor(AccessTokenProperties accessTokenProperties) {
        return new ReactiveDefaultTokenExtractor(accessTokenProperties);
    }

}