package com.ark.component.security.autoconfigure.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT配置属性
 */
@Data
@ConfigurationProperties(prefix = "ark.security.jwt")
public class JwtProperties {
    
    /**
     * RSA公钥
     */
    private String publicKey;
    
    /**
     * RSA私钥
     */
    private String privateKey;
} 