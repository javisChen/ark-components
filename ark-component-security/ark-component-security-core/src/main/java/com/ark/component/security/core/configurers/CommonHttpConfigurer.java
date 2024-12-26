package com.ark.component.security.core.configurers;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * ARK框架通用安全配置
 * 提供所有微服务统一的Spring Security配置，包括：
 * 1. 禁用Session - 采用无状态JWT Token认证
 * 2. 禁用CSRF - 采用Token认证不需要CSRF防护
 * 3. 禁用匿名访问 - 要求所有请求必须认证
 * 4. 配置Redis会话存储 - 支持分布式会话管理
 * 5. 配置Swagger文档接口白名单
 * 6. 配置权限校验策略 - 所有请求默认放行，由网关统一调用认证中心进行权限校验
 *
 * 使用方式:
 * http.apply(new CommonHttpConfigurer())
 *
 * @author JC
 */
public final class CommonHttpConfigurer extends AbstractHttpConfigurer<CommonHttpConfigurer, HttpSecurity> {

    private ApplicationContext context;

    /**
     * 初始化配置
     * 1. 获取Spring上下文
     * 2. 配置Redis会话存储
     * 3. 配置基础安全策略
     * 4. 配置接口白名单
     * 5. 配置权限校验策略
     */
    @Override
    public void init(HttpSecurity http) throws Exception {
        context = http.getSharedObject(ApplicationContext.class);

        configureContextRepository(http);

        http
                // 禁用Session - 使用JWT Token认证
                .sessionManagement(AbstractHttpConfigurer::disable)
                // 禁用CSRF - Token认证不需要CSRF防护
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用匿名访问 - 要求认证
                .anonymous(AbstractHttpConfigurer::disable)
                // Swagger文档接口白名单
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/doc.html")
                        .permitAll()
                )
                // 权限校验策略
                // 默认放行所有请求，由网关统一调用认证中心的AccessController接口进行权限校验
                .authorizeHttpRequests(requestMatcherRegistry ->
                        requestMatcherRegistry
                                .anyRequest()
                                .permitAll()
                );
    }

    /**
     * 配置Redis会话存储
     * 从Spring容器获取SecurityContextRepository实现
     * 配置到HttpSecurity中用于存储认证信息
     */
    private void configureContextRepository(HttpSecurity http) throws Exception {
        SecurityContextRepository securityContextRepository = context.getBean(SecurityContextRepository.class);
        http.securityContext(configurer ->
                configurer.securityContextRepository(securityContextRepository)
        );
    }
}
