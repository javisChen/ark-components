package com.ark.component.security.core.token.generate;

import com.ark.component.security.base.user.AuthUser;
import com.ark.component.security.core.token.TokenMetadata;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JWT Token生成器实现
 */
@RequiredArgsConstructor
public class JwtTokenGenerator implements TokenGenerator {

    private final JwtEncoder jwtEncoder;
    private final SignatureAlgorithm jwsAlgorithm = SignatureAlgorithm.RS256;

    @Override
    public String generateToken(TokenMetadata metadata, AuthUser authUser) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
            // 基础信息
            .issuer(metadata.getIssuer())
            .issuedAt(metadata.getIssuedAt())
            .expiresAt(metadata.getExpiresAt())
            .subject(authUser.getUsername())
            .audience(Collections.emptyList())
            // 用户信息
//            .claim(LoginUser.USER_CODE, loginUser.getUserCode())
            .claim(AuthUser.USER_ID, authUser.getUserId())
            .claim(AuthUser.USERNAME, authUser.getUsername())
            .claim(AuthUser.IS_SUPER_ADMIN, authUser.getIsSuperAdmin())
            // 权限信息
            .claim("authorities", getAuthorities(authUser))
            .build();
                
        JwsHeader jwsHeader = JwsHeader.with(jwsAlgorithm).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    @Override
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * 提取用户权限列表
     */
    private List<String> getAuthorities(AuthUser authUser) {
        Set<GrantedAuthority> authorities = authUser.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            return Collections.emptyList();
        }
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    }
}
