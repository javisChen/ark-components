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

}
