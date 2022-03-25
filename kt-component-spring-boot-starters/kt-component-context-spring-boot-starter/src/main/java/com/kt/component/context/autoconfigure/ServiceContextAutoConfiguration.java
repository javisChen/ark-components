package com.kt.component.context.autoconfigure;

import com.kt.component.cache.redis.RedisCacheService;
import com.kt.component.context.interceptor.ServiceContextInterceptor;
import com.kt.component.context.token.AccessTokenConfig;
import com.kt.component.context.token.AccessTokenExtractor;
import com.kt.component.context.token.AccessTokenStandardExtractor;
import lombok.extern.slf4j.Slf4j;
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
        log.info("enable [kt-component-context-spring-boot-starter]");
    }

    @Bean
    public AccessTokenExtractor accessTokenExtractor() {
        return new AccessTokenStandardExtractor(new AccessTokenConfig());
    }

    @Bean
    public ServiceContextInterceptor serviceContextInterceptor(RedisCacheService redisService,
                                                               AccessTokenExtractor accessTokenExtractor) {
        return new ServiceContextInterceptor(redisService, accessTokenExtractor);
    }
}