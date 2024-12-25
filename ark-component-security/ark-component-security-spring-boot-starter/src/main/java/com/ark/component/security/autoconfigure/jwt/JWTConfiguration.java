package com.ark.component.security.autoconfigure.jwt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.ark.component.security.autoconfigure.SecurityProperties;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * JWT配置类
 * 提供JWT(JSON Web Token)的编码、解码及密钥管理等核心功能
 *
 * @author JC
 */
@EnableConfigurationProperties(JwtProperties.class)
public class JWTConfiguration {

    /**
     * 配置RSA密钥源
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(SecurityProperties properties) {
        String pri = properties.getJwt().getPrivateKey();
        String pub = properties.getJwt().getPublicKey();
        // 使用测试密钥对
        RSA rsa = SecureUtil.rsa(pri, pub);

        JWK rsaKey = new RSAKey.Builder((RSAPublicKey) rsa.getPublicKey())
                .privateKey(rsa.getPrivateKey())
                .keyID("rsa-" + UUID.randomUUID())
                .algorithm(JWSAlgorithm.RS256)
                .build();
        
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    /**
     * 格式化RSA密钥字符串
     * 移除所有换行符、空格和回车符
     */
    private String formatKey(String key) {
        if (StrUtil.isBlank(key)) {
            return key;
        }
        return key.replaceAll("[\\s\\r\\n]", "");
    }

    /**
     * JWT编码器
     */
    @Bean
    public NimbusJwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * JWT解码器
     */
    @Bean
    public NimbusJwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> jwsKeySelector = 
            new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        return new NimbusJwtDecoder(jwtProcessor);
    }
}
