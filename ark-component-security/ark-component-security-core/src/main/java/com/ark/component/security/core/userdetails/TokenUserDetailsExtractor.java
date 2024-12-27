package com.ark.component.security.core.userdetails;

import com.ark.component.security.base.user.LoginUser;

/**
 * Token用户信息提取器接口
 */
public interface TokenUserDetailsExtractor {
    
    /**
     * 从token中提取用户信息
     * @param token 访问令牌
     * @return 登录用户信息
     */
    LoginUser extractUserDetails(String token);
} 