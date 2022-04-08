package com.kt.component.context.core.token;

import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Token提取器默认实现
 * @author jc
 */
@Component
public class AccessTokenStandardExtractor implements AccessTokenExtractor {

    private final AccessTokenConfig tokenConfig;

    public AccessTokenStandardExtractor(AccessTokenConfig tokenConfig) {
        this.tokenConfig = tokenConfig;
    }

    @Override
    public String extract(HttpServletRequest request) {
        String accessToken = extractFromParameterMap(request, tokenConfig);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        String tokenHeaderPrefix = tokenConfig.getTokenHeaderPrefix();
        String tokenHeader = tokenConfig.getTokenHeader();
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
