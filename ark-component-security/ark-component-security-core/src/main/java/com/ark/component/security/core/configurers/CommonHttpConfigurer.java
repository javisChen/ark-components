package com.ark.component.security.core.configurers;

import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * ARK框架通用安全配置
 * 提供统一的安全配置，包括：
 * 1. JWT token验证
 * 2. Redis会话管理
 * 3. 访问控制过滤
 * 4. 请求链路追踪
 * 5. 统一的异常处理
 *
 * 使用方式:
 * http.apply(new ArkSecurityConfigurer())
 *
 * @author JC
 */
public final class CommonHttpConfigurer
        extends AbstractHttpConfigurer<CommonHttpConfigurer, HttpSecurity> {

    private ApplicationContext context;

    /**
     * 初始化配置，设置安全上下文仓库
     */
    @Override
    public void init(HttpSecurity http) throws Exception {
        http
                // 暂时禁用SessionManagement
                .sessionManagement(AbstractHttpConfigurer::disable)
                // 禁用csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用匿名登录
                .anonymous(AbstractHttpConfigurer::disable)
                // 公共白名单
                .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/doc.html")
                        .permitAll()

                )
                // 权限拦截全部交给认证中心AccessController接口处理，由网关调用
                .authorizeHttpRequests(requestMatcherRegistry ->
                        requestMatcherRegistry
                                .anyRequest()
                                .permitAll()
                );

    }

}
