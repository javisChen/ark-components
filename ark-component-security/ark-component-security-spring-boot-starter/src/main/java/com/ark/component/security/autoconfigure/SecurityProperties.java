package com.ark.component.security.autoconfigure;

import com.ark.component.security.autoconfigure.jwt.JwtProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 安全配置属性
 */
@Data
@ConfigurationProperties(prefix = "ark.security")
public class SecurityProperties {
    
    /**
     * JWT配置
     */
    private JwtProperties jwt = new JwtProperties();
}
