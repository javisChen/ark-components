package com.ark.component.security.core.configurers;

import com.ark.component.security.core.authentication.AuthenticationErrorHandler;
import com.ark.component.security.core.authentication.filter.AccessCheckFilter;
import com.ark.component.security.core.authentication.filter.TraceFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;

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
public final class ArkServiceHttpConfigurer
        extends AbstractHttpConfigurer<ArkServiceHttpConfigurer, HttpSecurity> {

    private ApplicationContext context;

    /**
     * 初始化配置，设置安全上下文仓库
     */
    @Override
    public void init(HttpSecurity http) throws Exception {
        context = http.getSharedObject(ApplicationContext.class);
        
        SecurityContextRepository securityContextRepository = context.getBean(SecurityContextRepository.class);
        http.securityContext(configurer ->
                configurer.securityContextRepository(securityContextRepository)
        );
    }

    /**
     * 配置安全组件
     * 1. 注册安全上下文仓库
     * 2. 配置错误处理器
     * 3. 添加安全过滤器
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {

        AuthenticationErrorHandler errorHandler = new AuthenticationErrorHandler();
        http.setSharedObject(AuthenticationErrorHandler.class, errorHandler);

        configureFilters(http, errorHandler);

        registerErrorHandler(http, errorHandler);
    }

    /**
     * 添加安全过滤器链
     * TraceFilter -> AccessCheckFilter -> SecurityContextHolderFilter
     */
    private void configureFilters(HttpSecurity http, AuthenticationErrorHandler errorHandler) {
        AccessCheckFilter accessCheckFilter = new AccessCheckFilter(context, errorHandler);
        http.addFilterBefore(accessCheckFilter, SecurityContextHolderFilter.class);

        TraceFilter traceFilter = new TraceFilter();
        http.addFilterBefore(traceFilter, AccessCheckFilter.class);
    }
    /**
     * 注册统一的错误处理器
     */
    private void registerErrorHandler(HttpSecurity http, AuthenticationErrorHandler errorHandler) 
            throws Exception {
        http.exceptionHandling(configurer -> configurer
                .accessDeniedHandler(errorHandler)
                .authenticationEntryPoint(errorHandler)
        );
    }

//    /**
//     * 获取JWT解码器
//     */
//    private JwtDecoder getJwtDecoder() {
//        Set<JWSAlgorithm> jwsAlg = new HashSet<>();
//        jwsAlg.addAll(JWSAlgorithm.Family.RSA);
//        jwsAlg.addAll(JWSAlgorithm.Family.EC);
//        jwsAlg.addAll(JWSAlgorithm.Family.HMAC_SHA);
//
//        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
//        JWSKeySelector<SecurityContext> jwsKeySelector =
//            new JWSVerificationKeySelector<>(jwsAlg, jwkSource());
//        jwtProcessor.setJWSKeySelector(jwsKeySelector);
//        return new NimbusJwtDecoder(jwtProcessor);
//    }

//    /**
//     * 获取JWT密钥源
//     */
//    private JWKSource<SecurityContext> jwkSource() {
//        OctetSequenceKey key = new OctetSequenceKey.Builder(
//                JWT_SIGN_SECRET.getBytes(StandardCharsets.UTF_8))
//                .keyID(JWT_KEY_ID)
//                .algorithm(JWSAlgorithm.HS256)
//                .build();
//        JWKSet jwkSet = new JWKSet(key);
//        return new ImmutableJWKSet<>(jwkSet);
//    }
}
