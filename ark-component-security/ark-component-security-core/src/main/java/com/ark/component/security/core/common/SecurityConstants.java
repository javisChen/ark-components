package com.ark.component.security.core.common;

/**
 * 安全变量
 */
public interface SecurityConstants {

    /**
     * 用户密码盐值
     */
    String PASSWORD_SALT = "6r4ogrp7th9rbyob";

    /**
     * TOKEN过期时间（秒）
     */
    Long TOKEN_EXPIRES_SECONDS = (long) (60 * 60 * 24 * 7);

    /**
     * JWT签名Secret
     */
    String JWT_SIGN_SECRET = "QnZyMXl4WGE5MFpLT2pSa1k1Y2FFZ1FDU1dMNUN1b2YySTAzMkx0ajJsVQ";

    /**
     * JWT KeyID
     */
    String JWT_KEY_ID = "8d8bf81d-5032-4aeb-8962-ef473fd21ab1";

    /**
     * 请求来源Header
     */
    String HTTP_HEADER_SOURCE = "ark-access-from";

    /**
     * 网关请求标识
     */
    String ACCESS_SOURCE_GATEWAY = "ark-gateway";

    /**
     * 服务间调用标识
     */
    String ACCESS_SOURCE_SERVICE = "ark-service";

    /**
     * 值：登录用户信息
     * 作用：用户根据token获取登录用户信息
     * 完整：iam:lg:tk:{token}
     */
    String CACHE_KEY_PREFIX_LOGIN_USER_ACCESS_TOKEN = "login:user:info:";
    /**
     * 值：用户登录时分配的AccessToken
     * 作用：踢出用户。根据userId查到token -> 根据token移除登录缓存
     * 完整：iam:lg:uid:{userId}
     */
    String CACHE_KEY_PREFIX_LOGIN_USER_ID = "login:user:token:";
}
