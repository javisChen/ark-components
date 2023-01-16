package com.ark.component.context.autoconfigure;

import com.ark.component.context.core.interceptor.ServiceContextInterceptor;
import com.ark.component.context.core.resolver.JwtUserResolver;
import com.ark.component.context.core.resolver.UserResolver;
import com.ark.component.security.base.token.AccessTokenProperties;
import com.ark.component.security.base.token.extractor.AccessTokenExtractor;
import com.ark.component.security.core.token.extractor.DefaultTokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
        log.info("enable [ark-component-context-spring-boot-starter]");
    }

    @Bean
    public AccessTokenExtractor accessTokenExtractor() {
        return new DefaultTokenExtractor(new AccessTokenProperties());
    }

    @Bean
    @ConditionalOnMissingBean
    public UserResolver userResolver() {
        return new JwtUserResolver();
    }

    @Bean
    public ServiceContextInterceptor serviceContextInterceptor(AccessTokenExtractor tokenExtractor,
                                                               UserResolver userResolver) {
        return new ServiceContextInterceptor(tokenExtractor, userResolver);
    }
}