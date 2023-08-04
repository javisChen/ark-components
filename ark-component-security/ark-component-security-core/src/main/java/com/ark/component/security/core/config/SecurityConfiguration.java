package com.ark.component.security.core.config;

import com.ark.component.cache.CacheService;
import com.ark.component.security.core.context.repository.RedisSecurityContextRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextRepository;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import static com.ark.component.security.core.config.SecurityConstants.JWT_SIGN_SECRET;

public class SecurityConfiguration {

    private KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     *
     * @return
     */
    public static void main(String[] args) throws JOSEException {
        OctetSequenceKey generate = new OctetSequenceKeyGenerator(256)
                .keyID(UUID.randomUUID().toString()) // give the key some ID (optional)
                .algorithm(JWSAlgorithm.HS256) // indicate the intended key alg (optional)
                .issueTime(new Date()) // issued-at timestamp (optional)
                .generate();
        OctetSequenceKey jwk = generate;
        System.out.println(jwk);
        System.out.println(jwk.toSecretKey().toString());
    }

    @Bean
    @ConditionalOnMissingBean(JWKSource.class)
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        OctetSequenceKey key = new OctetSequenceKey.Builder(JWT_SIGN_SECRET.getBytes(StandardCharsets.UTF_8))
//                .keyID(UUID.randomUUID().toString())
                .algorithm(JWSAlgorithm.HS256).build();
        JWKSet jwkSet = new JWKSet(key);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityContextRepository.class)
    private SecurityContextRepository securityContextRepository(CacheService cacheService,
                                                                JWKSource<SecurityContext> jwkSource) {
        Set<JWSAlgorithm> jwsAlg = new HashSet<>();
        jwsAlg.addAll(JWSAlgorithm.Family.RSA);
        jwsAlg.addAll(JWSAlgorithm.Family.EC);
        jwsAlg.addAll(JWSAlgorithm.Family.HMAC_SHA);
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlg, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        JwtDecoder jwtDecoder = new NimbusJwtDecoder(jwtProcessor);
        return new RedisSecurityContextRepository(cacheService, jwtDecoder);
    }

    @Bean
    public SecurityFilterChain serviceSecurityFilterChain(HttpSecurity httpSecurity,
                                                          SecurityProperties securityProperties,
                                                          SecurityContextRepository securityContextRepository) throws Exception {

        httpSecurity.securityContext(configurer -> configurer.securityContextRepository(securityContextRepository));

        httpSecurity
                // 暂时禁用SessionManagement
                .sessionManagement(AbstractHttpConfigurer::disable)
                // 禁用csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁用匿名登录
                .anonymous(AbstractHttpConfigurer::disable);

        // 白名单
        List<String> allowList = securityProperties.getAllowList();
        // 资源权限控制
        httpSecurity.authorizeHttpRequests(requests ->
                requests
                        .requestMatchers(allowList.stream().toList().toArray(new String[0]))
                        .permitAll()
                        .anyRequest()
                        .authenticated()
        );
        return httpSecurity.build();
    }
}
