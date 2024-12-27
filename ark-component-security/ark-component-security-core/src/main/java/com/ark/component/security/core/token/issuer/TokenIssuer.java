package com.ark.component.security.core.token.issuer;

import com.ark.component.security.base.user.LoginUser;
import com.ark.component.security.core.authentication.LoginAuthenticationToken;
import com.ark.component.security.core.common.SecurityConstants;
import com.ark.component.security.core.token.TokenMetadata;
import com.ark.component.security.core.token.generator.TokenGenerator;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class TokenIssuer {
    
    private final TokenGenerator tokenGenerator;
    
    public LoginAuthenticationToken issueToken(LoginUser loginUser) {
        // 1. 生成token元数据
        TokenMetadata metadata = createTokenMetadata();
        
        // 2. 生成token
        String token = tokenGenerator.generateToken(metadata, loginUser);
        String refreshToken = tokenGenerator.generateRefreshToken();
        
        // 3. 返回认证信息
        return new LoginAuthenticationToken(
            loginUser,
            token,
            refreshToken,
            metadata.getExpiresIn()
        );
    }
    
    private TokenMetadata createTokenMetadata() {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(SecurityConstants.TOKEN_EXPIRES_SECONDS, ChronoUnit.SECONDS);
        
        return TokenMetadata.builder()
                .issuer("auth")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .expiresIn(SecurityConstants.TOKEN_EXPIRES_SECONDS)
                .build();
    }
} 