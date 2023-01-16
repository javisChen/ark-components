package com.ark.component.security.base.token;

import lombok.Data;

/**
 * Token基础配置
 * @author javis
 */
@Data
public class AccessTokenProperties {

    /**
     * token的Header
     */
    private String tokenHeader = "Authorization";

    /**
     * 认证Token前缀
     */
    private String tokenHeaderPrefix = "Bearer ";

    /**
     * 认证Token查询参数的key
     */
    private String tokenQueryParam = "accessToken";
}
