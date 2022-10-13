package com.ark.component.context.core.token;

import javax.servlet.http.HttpServletRequest;

/**
 * Token提取，可自行实现提取方法
 * @author victor
 **/
public interface AccessTokenExtractor {

    /**
     * 从request中提取token
     * @param request HttpServletRequest
     * @return token
     */
    String extract(HttpServletRequest request);
}
