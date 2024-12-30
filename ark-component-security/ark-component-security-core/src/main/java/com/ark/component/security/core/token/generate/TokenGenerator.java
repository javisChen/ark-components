package com.ark.component.security.core.token.generate;

import com.ark.component.security.base.user.LoginUser;
import com.ark.component.security.core.token.TokenMetadata;

public interface TokenGenerator {
    /**
     * 根据元数据生成token
     */
    String generateToken(TokenMetadata metadata, LoginUser loginUser);
    
    /**
     * 生成刷新token
     */
    String generateRefreshToken();
}
