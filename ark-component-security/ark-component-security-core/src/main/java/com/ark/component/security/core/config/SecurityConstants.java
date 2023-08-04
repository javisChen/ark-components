package com.ark.component.security.core.config;

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
    long TOKEN_EXPIRES_SECONDS = 60 * 60 * 24 * 7;

    /**
     * JWT签名Secret
     */
    String JWT_SIGN_SECRET = "QnZyMXl4WGE5MFpLT2pSa1k1Y2FFZ1FDU1dMNUN1b2YySTAzMkx0ajJsVQ";


}
