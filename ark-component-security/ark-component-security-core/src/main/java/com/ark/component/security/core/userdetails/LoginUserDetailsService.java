package com.ark.component.security.core.userdetails;

import com.ark.component.security.base.user.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@RequiredArgsConstructor
@Slf4j
public class LoginUserDetailsService implements UserDetailsService {

    private final JwtDecoder jwtDecoder;

    public AuthUser loadUserByCredential(String credential) throws UsernameNotFoundException {

        Jwt jwt = decodeToken(credential);

        AuthUser authUser = new AuthUser();
        authUser.setUserId(Long.parseLong(jwt.getClaimAsString(AuthUser.USER_ID)));
        authUser.setUserCode(jwt.getClaimAsString(AuthUser.USER_CODE));
        authUser.setUsername(jwt.getClaimAsString(AuthUser.USERNAME));
        authUser.setIsSuperAdmin(jwt.getClaimAsBoolean(AuthUser.IS_SUPER_ADMIN));
        // LoginUser loginUser = convert(values);
        return authUser;
    }

    private Jwt decodeToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.error("Failed to decode JWT", e);
            throw new BadCredentialsException("无效凭证");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
