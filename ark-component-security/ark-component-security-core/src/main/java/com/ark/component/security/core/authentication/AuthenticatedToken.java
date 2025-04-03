package com.ark.component.security.core.authentication;

import com.ark.component.security.base.authentication.AuthPrincipal;
import com.ark.component.security.base.authentication.Token;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 已认证的令牌
 * 用于存储认证成功后的认证信息
 *
 * @author JC
 * @since 2024-01-01
 */
@Getter
public class AuthenticatedToken extends AbstractAuthenticationToken {

    /**
     * 令牌信息
     * 包含访问令牌、刷新令牌等信息
     */
    private final Token token;

    /**
     * 登录用户信息
     * 包含用户基本信息、权限等
     */
    private final AuthPrincipal authPrincipal;

    /**
     * 创建登录认证令牌
     *
     * @param authPrincipal 登录用户信息
     * @param token   令牌信息
     */
    public AuthenticatedToken(AuthPrincipal authPrincipal, Token token) {
        super(authPrincipal.getAuthorities());
        this.authPrincipal = authPrincipal;
        this.token = token;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return token.getAccessToken();
    }

    @Override
    public Object getPrincipal() {
        return this.authPrincipal;
    }

    public static AuthenticatedToken authenticated(AuthPrincipal authPrincipal, Token token) {
        return new AuthenticatedToken(authPrincipal, token);
    }
}