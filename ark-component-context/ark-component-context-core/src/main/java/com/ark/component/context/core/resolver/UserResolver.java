package com.ark.component.context.core.resolver;

import com.ark.component.context.core.LoginUserContext;

/**
 * 访问用户解析
 */
public interface UserResolver {

    /**
     * 根据令牌解析出访问用户的基本信息
     * @param token 令牌
     * @return 访问用户上下文信息
     */
    LoginUserContext resolve(String token);
}
