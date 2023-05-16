package com.ark.component.security.core.token.extractor;

import cn.hutool.core.collection.CollUtil;
import com.ark.component.security.base.token.AccessTokenProperties;
import com.ark.component.security.base.token.extractor.AccessTokenExtractor;
import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Token提取器默认实现
 * @author jc
 */
public class DefaultTokenExtractor implements AccessTokenExtractor<HttpServletRequest> {

    private final AccessTokenProperties tokenProperties;

    public DefaultTokenExtractor(AccessTokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public String extract(HttpServletRequest request) {
        String accessToken = extractFromParameterMap(request);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        return StringUtils.substringAfter(request.getHeader(tokenProperties.getTokenHeader()), tokenProperties.getTokenHeaderPrefix());
    }

    private String extractFromParameterMap(HttpServletRequest request) {
        if (CollUtil.isNotEmpty(request.getParameterMap())) {
            String[] tokenParamVal = request.getParameterMap().get(tokenProperties.getTokenQueryParam());
            if (tokenParamVal != null && tokenParamVal.length > 0) {
                return tokenParamVal[0];
            }
        }
        return "";
    }

}
