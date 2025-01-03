package com.ark.component.security.core.authentication;

import com.ark.component.security.base.user.AuthUser;
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
     * 访问令牌
     * 用于后续请求的身份验证
     */
    private final String accessToken;

    /**
     * 刷新令牌
     * 用于在访问令牌过期时获取新的访问令牌
     */
    private final String refreshToken;

    /**
     * 访问令牌过期时间
     * 单位：秒
     */
    private final Long expiresIn;

    /**
     * 令牌类型
     * 固定值："Bearer"，符合OAuth2规范
     */
    private final String tokenType;

    /**
     * 登录用户信息
     * 包含用户基本信息、权限等
     */
    private final AuthUser authUser;

    /**
     * 创建登录认证令牌
     *
     * @param authUser     登录用户信息
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param expiresIn    过期时间(秒)
     */
    public AuthenticatedToken(AuthUser authUser,
                              String accessToken,
                              String refreshToken,
                              Long expiresIn) {
        super(authUser.getAuthorities());
        this.authUser = authUser;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
        super.setAuthenticated(true); // must use super, as we override

    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getPrincipal() {
        return this.authUser;
    }

//    public static AuthenticatedToken unauthenticated(String principal, String credentials) {
//        return new AuthenticatedToken(principal, credentials);
//    }

    public static AuthenticatedToken authenticated(AuthUser authUser,
                                                   String accessToken,
                                                   String refreshToken,
                                                   Long expiresIn) {
        return new AuthenticatedToken(authUser, accessToken, refreshToken, expiresIn);
    }
}