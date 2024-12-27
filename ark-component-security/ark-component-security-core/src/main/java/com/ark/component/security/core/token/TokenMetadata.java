package com.ark.component.security.core.token;

import lombok.Builder;
import lombok.Getter;
import java.time.Instant;

/**
 * Token元数据
 * 包含Token的基础信息，如签发人、签发时间、过期时间等
 *
 * @author JC
 */
@Getter
@Builder
public class TokenMetadata {
    
    /**
     * 签发人
     */
    private final String issuer;
    
    /**
     * 签发时间
     */
    private final Instant issuedAt;
    
    /**
     * 过期时间
     */
    private final Instant expiresAt;
    
    /**
     * 有效期(秒)
     */
    private final Long expiresIn;
} 