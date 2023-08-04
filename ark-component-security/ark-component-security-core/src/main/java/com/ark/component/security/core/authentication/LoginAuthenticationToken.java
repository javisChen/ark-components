package com.ark.component.security.core.authentication;

import com.ark.component.security.base.user.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String accessToken;
    private final LoginUser loginUser;

    public LoginAuthenticationToken(LoginUser loginUser, String accessToken) {
        super(loginUser, loginUser.getPassword(), loginUser.getAuthorities());
        this.accessToken = accessToken;
        this.loginUser = loginUser;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

}
