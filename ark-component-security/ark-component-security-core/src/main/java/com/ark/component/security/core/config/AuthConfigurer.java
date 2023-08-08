package com.ark.component.security.core.config;

import com.ark.component.security.core.authentication.AuthenticationErrorHandler;
import com.ark.component.security.core.authentication.filter.AccessCheckFilter;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextRepository;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static com.ark.component.security.core.config.SecurityConstants.JWT_KEY_ID;
import static com.ark.component.security.core.config.SecurityConstants.JWT_SIGN_SECRET;

public final class AuthConfigurer
        extends AbstractHttpConfigurer<AuthConfigurer, HttpSecurity> {

    private ApplicationContext context;

    @Override
    public void init(HttpSecurity http) throws Exception {
        context = http.getSharedObject(ApplicationContext.class);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        if (context != null) {
            registerDefaultSecurityContextRepository(http);
        }

        AuthenticationErrorHandler errorHandler = new AuthenticationErrorHandler();

        // addFilters(http, errorHandler);

        registerErrorHandler(http, errorHandler);

    }

    private void addFilters(HttpSecurity http, AuthenticationErrorHandler errorHandler) {
        AccessCheckFilter accessCheckFilter = new AccessCheckFilter(errorHandler);
        http.addFilterBefore(accessCheckFilter, SecurityContextHolderFilter.class);
    }

    private void registerDefaultSecurityContextRepository(HttpSecurity http) throws Exception {
        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
        http.securityContext(configurer -> configurer.securityContextRepository(securityContextRepository));
    }

    private void registerErrorHandler(HttpSecurity http, AuthenticationErrorHandler errorHandler) throws Exception {
        // 权限不足时的处理
        http.exceptionHandling(configurer -> configurer
                .accessDeniedHandler(errorHandler)
                .authenticationEntryPoint(errorHandler)
        );
    }

    private JwtDecoder getJwtDecoder() {
        Set<JWSAlgorithm> jwsAlg = new HashSet<>();
        jwsAlg.addAll(JWSAlgorithm.Family.RSA);
        jwsAlg.addAll(JWSAlgorithm.Family.EC);
        jwsAlg.addAll(JWSAlgorithm.Family.HMAC_SHA);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlg, jwkSource());
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        return new NimbusJwtDecoder(jwtProcessor);
    }

    public JWKSource<SecurityContext> jwkSource() {
//        KeyPair keyPair = generateRsaKey();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        RSAKey rsaKey = new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
        OctetSequenceKey key = new OctetSequenceKey.Builder(JWT_SIGN_SECRET.getBytes(StandardCharsets.UTF_8))
                .keyID(JWT_KEY_ID)
                .algorithm(JWSAlgorithm.HS256).build();
        JWKSet jwkSet = new JWKSet(key);
        return new ImmutableJWKSet<>(jwkSet);
    }
}
