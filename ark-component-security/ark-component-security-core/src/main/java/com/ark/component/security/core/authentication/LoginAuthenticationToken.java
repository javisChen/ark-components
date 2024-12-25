package com.ark.component.security.core.authentication;

import com.ark.component.security.base.user.LoginUser;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 登录认证令牌
 * 继承自UsernamePasswordAuthenticationToken，用于存储登录成功后的认证信息
 *
 * @author JC
 * @since 2024-01-01
 */
@Getter
public class LoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

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
    private final LoginUser loginUser;

    /**
     * 创建登录认证令牌
     *
     * @param loginUser    登录用户信息
     * @param accessToken  访问令牌
     * @param refreshToken 刷新令牌
     * @param expiresIn    过期时间(秒)
     */
    public LoginAuthenticationToken(LoginUser loginUser, 
                                  String accessToken,
                                  String refreshToken,
                                  long expiresIn) {
        super(loginUser, loginUser.getPassword(), loginUser.getAuthorities());
        this.loginUser = loginUser;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
    }
}