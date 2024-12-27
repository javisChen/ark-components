package com.ark.component.security.core.userdetails;

import com.ark.component.security.base.user.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenUserDetailsExtractor implements TokenUserDetailsExtractor {

    private final JwtDecoder jwtDecoder;

    @Override
    public LoginUser extractUserDetails(String token) {
        Jwt jwt = decodeToken(token);
        
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(Long.parseLong(jwt.getClaimAsString(LoginUser.USER_ID)));
        loginUser.setUserCode(jwt.getClaimAsString(LoginUser.USER_CODE));
        loginUser.setUsername(jwt.getClaimAsString(LoginUser.USERNAME));
        loginUser.setIsSuperAdmin(jwt.getClaimAsBoolean(LoginUser.IS_SUPER_ADMIN));
        
        return loginUser;
    }

    private Jwt decodeToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.error("JWT解析失败", e);
            throw new BadCredentialsException("无效凭证");
        }
    }
} 