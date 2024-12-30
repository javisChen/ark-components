package com.ark.component.security.core.userdetails;

import com.ark.component.security.base.user.LoginUser;
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

    public LoginUser loadUserByCredential(String credential) throws UsernameNotFoundException {

        Jwt jwt = decodeToken(credential);

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(Long.parseLong(jwt.getClaimAsString(LoginUser.USER_ID)));
        loginUser.setUserCode(jwt.getClaimAsString(LoginUser.USER_CODE));
        loginUser.setUsername(jwt.getClaimAsString(LoginUser.USERNAME));
        loginUser.setIsSuperAdmin(jwt.getClaimAsBoolean(LoginUser.IS_SUPER_ADMIN));
        // LoginUser loginUser = convert(values);
        return loginUser;
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
