package com.ark.component.security.core.token.issuer;

import com.ark.component.security.base.user.AuthUser;
import com.ark.component.security.core.authentication.LoginAuthenticationToken;
import com.ark.component.security.core.common.SecurityConstants;
import com.ark.component.security.core.token.TokenMetadata;
import com.ark.component.security.core.token.generate.TokenGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Token签发器
 * 负责生成和签发认证Token，包括：
 * 1. 访问令牌(Access Token) - 用于身份验证
 * 2. 刷新令牌(Refresh Token) - 用于刷新访问令牌
 *
 * @author JC
 */
@Slf4j
@RequiredArgsConstructor
public class TokenIssuer {
    
    /**
     * Token生成器，由具体实现类提供生成算法
     */
    private final TokenGenerator tokenGenerator;

    /**
     * Token签发者标识
     */
    private final static String DEFAULT_ISSUER = "AuthCenter";

    /**
     * 为登录用户签发Token
     * 包含访问令牌和刷新令牌
     *
     * @param authUser 登录用户信息
     * @return 登录认证令牌，包含访问令牌和刷新令牌
     */
    public LoginAuthenticationToken issueToken(AuthUser authUser) {
        Assert.notNull(authUser, "LoginUser cannot be null");
        
        if (log.isDebugEnabled()) {
            log.debug("Begin issuing token for user: {}", authUser.getUsername());
        }
        
        try {
            // 1. 生成token元数据
            TokenMetadata metadata = createTokenMetadata();
            if (log.isDebugEnabled()) {
                log.debug("Created token metadata - issuer: {}, expiresIn: {}s", 
                    metadata.getIssuer(), metadata.getExpiresIn());
            }
            
            // 2. 生成访问令牌和刷新令牌
            String accessToken = tokenGenerator.generateToken(metadata, authUser);
            String refreshToken = tokenGenerator.generateRefreshToken();
            
            // 3. 返回认证信息
            LoginAuthenticationToken authToken = new LoginAuthenticationToken(
                    authUser,
                accessToken,
                refreshToken,
                metadata.getExpiresIn()
            );
            
            log.info("Successfully issued token for user: {}", authUser.getUsername());
            
            if (log.isTraceEnabled()) {
                log.trace("Token details - user: {}, expiresIn: {}s", 
                    authUser.getUsername(), metadata.getExpiresIn());
            }
            
            return authToken;
            
        } catch (Exception e) {
            log.error("Failed to issue token for user: {}", authUser.getUsername(), e);
            throw new IllegalStateException("Token generation failed", e);
        }
    }
    
    /**
     * 创建Token元数据
     * 包含签发人、签发时间、过期时间等基础信息
     *
     * @return Token元数据对象
     */
    private TokenMetadata createTokenMetadata() {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(SecurityConstants.TOKEN_EXPIRES_SECONDS, ChronoUnit.SECONDS);

        return TokenMetadata.builder()
                .issuer(DEFAULT_ISSUER)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .expiresIn(SecurityConstants.TOKEN_EXPIRES_SECONDS)
                .build();
    }
} 