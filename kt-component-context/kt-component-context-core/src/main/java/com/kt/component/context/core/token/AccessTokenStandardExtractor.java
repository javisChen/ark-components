package com.kt.component.context.core.token;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Token提取器默认实现
 * @author jc
 */
public class AccessTokenStandardExtractor implements AccessTokenExtractor {

    private final AccessTokenConfig accessTokenConfig;

    public AccessTokenStandardExtractor(AccessTokenConfig accessTokenConfig) {
        this.accessTokenConfig = accessTokenConfig;
    }

    @Override
    public String extract(HttpServletRequest request) {
        String accessToken = extractFromParameterMap(request, accessTokenConfig);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        String tokenHeaderPrefix = accessTokenConfig.getTokenHeaderPrefix();
        String tokenHeader = accessTokenConfig.getTokenHeader();
        accessToken = request.getHeader(tokenHeader);
        accessToken = StringUtils.substringAfter(accessToken, tokenHeaderPrefix);
        return accessToken;
    }

    private String extractFromParameterMap(HttpServletRequest request, AccessTokenConfig properties) {
        String token = null;
        if (CollUtil.isNotEmpty(request.getParameterMap())) {
            String[] tokenParamVal = request.getParameterMap().get(properties.getTokenQueryParam());
            if (tokenParamVal != null && tokenParamVal.length > 0) {
                token = tokenParamVal[0];
            }
        }
        return token;
    }
}
