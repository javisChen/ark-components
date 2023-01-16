package com.ark.component.security.base.token.extractor;

/**
 * Token提取，可自行实现提取方法
 * @author victor
 **/
public interface AccessTokenExtractor<T> {

    /**
     * 从request中提取token
     * @param request HttpServletRequest
     * @return token
     */
    String extract(T request);
}
