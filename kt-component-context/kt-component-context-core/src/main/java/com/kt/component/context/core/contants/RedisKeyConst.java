package com.kt.component.context.core.contants;

public interface RedisKeyConst {

    /**
     * 值：登录用户信息
     * 作用：用户根据token获取登录用户信息
     * 完整：iam:lg:tk:{token}
     */
    String LOGIN_USER_ACCESS_TOKEN_KEY_PREFIX = "iam:lg:tk:";

    /**
     * 值：用户登录时分配的AccessToken
     * 作用：踢出用户。根据userId查到token -> 根据token移除登录缓存
     * 完整：iam:lg:uid:{userId}
     */
    String LOGIN_USER_ID_KEY_PREFIX = "iam:lg:uid:";
}
