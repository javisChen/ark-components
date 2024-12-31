package com.ark.component.security.core.token.generate;

import com.ark.component.security.base.user.AuthUser;
import com.ark.component.security.core.token.TokenMetadata;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * UUID Token生成器
 * 使用UUID作为token的实现，适用于简单场景
 *
 * @author JMox
 */
@Component
public class UuidTokenGenerator implements TokenGenerator {
    
    @Override
    public String generateToken(TokenMetadata metadata, AuthUser authUser) {
        // 生成UUID并移除横线
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
} 