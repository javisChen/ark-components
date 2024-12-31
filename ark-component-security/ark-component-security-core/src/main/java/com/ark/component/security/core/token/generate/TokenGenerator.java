package com.ark.component.security.core.token.generate;

import com.ark.component.security.base.user.AuthUser;
import com.ark.component.security.core.token.TokenMetadata;

public interface TokenGenerator {
    /**
     * 根据元数据生成token
     */
    String generateToken(TokenMetadata metadata, AuthUser authUser);
    
    /**
     * 生成刷新token
     */
    String generateRefreshToken();
}
