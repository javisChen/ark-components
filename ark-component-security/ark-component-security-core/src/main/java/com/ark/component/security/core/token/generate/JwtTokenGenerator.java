package com.ark.component.security.core.token.generate;

import com.ark.component.security.base.user.LoginUser;
import com.ark.component.security.core.token.TokenMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.util.Collections;
import java.util.List;
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
    public String generateToken(TokenMetadata metadata, LoginUser loginUser) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
            // 基础信息
            .issuer(metadata.getIssuer())
            .issuedAt(metadata.getIssuedAt())
            .expiresAt(metadata.getExpiresAt())
            .subject(loginUser.getUsername())
            .audience(Collections.emptyList())
            // 用户信息
            .claim(LoginUser.USER_CODE, loginUser.getUserCode())
            .claim(LoginUser.USER_ID, loginUser.getUserId())
            .claim(LoginUser.USERNAME, loginUser.getUsername())
            .claim(LoginUser.IS_SUPER_ADMIN, loginUser.getIsSuperAdmin())
            // 权限信息
            .claim("authorities", getAuthorities(loginUser))
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
    private List<String> getAuthorities(LoginUser loginUser) {
        return loginUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    }
}
