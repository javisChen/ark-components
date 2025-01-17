package com.ark.component.security.core.service;

import com.ark.component.security.base.user.AuthUser;

/**
 * 认证用户服务接口
 * 定义了获取认证用户信息的方法
 */
public interface AuthUserService {

    /**
     * 根据访问令牌获取认证用户信息
     *
     * @param accessToken 访问令牌
     * @return 认证用户信息，如果未找到则返回null
     */
    AuthUser getAuthUser(String accessToken);
} 