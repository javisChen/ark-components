package com.ark.component.context.core;

import lombok.Data;

/**
 * 当前用户上下文
 */
@Data
public class LoginUserContext {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户名
     */
    private String username;

    /**
     * 令牌
     */
    private String accessToken;

    /**
     * 有效期
     */
    private Long expires;

    /**
     * 是否超级管理员
     */
    private Boolean isSuperAdmin;

}
