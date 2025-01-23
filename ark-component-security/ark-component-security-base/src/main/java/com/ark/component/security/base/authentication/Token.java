package com.ark.component.security.base.authentication;

import lombok.Getter;

/**
 * 认证令牌信息
 * 封装认证相关的令牌信息，包括访问令牌、刷新令牌等
 *
 * @author JC
 * @since 2024-01-01
 */
@Getter
public class Token {

    public static final String APP_CODE = "appCode";

    public static final String APP_TYPE = "appType";


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
     * 应用编码
     * 标识应用的唯一代码
     */
    private final String appCode;

    /**
     * 应用类型
     * 标识应用的类型
     */
    private final String appType;

    public Token(String accessToken, String refreshToken, Long expiresIn, String appCode, String appType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
        this.appCode = appCode;
        this.appType = appType;
    }

    /**
     * 创建令牌
     */
    public static Token of(String accessToken, String refreshToken, Long expiresIn, String appCode, String appType) {
        return new Token(accessToken, refreshToken, expiresIn, appCode, appType);
    }
} 