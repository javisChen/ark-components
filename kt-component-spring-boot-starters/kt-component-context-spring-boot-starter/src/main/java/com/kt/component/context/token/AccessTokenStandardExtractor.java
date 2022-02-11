package com.kt.component.context.token;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Token提取器默认实现
 * @author jc
 */
@Component
public class AccessTokenStandardExtractor implements AccessTokenExtractor {

    private final AccessTokenConfig properties;

    public AccessTokenStandardExtractor(AccessTokenConfig properties) {
        this.properties = properties;
    }

    @Override
    public String extract(HttpServletRequest request) {
        String token = extractFromParameterMap(request, properties);
        if (!StringUtils.isEmpty(token)) {
            return token;
        }
        String headerPrefix = properties.getTokenHeaderPrefix();
        String header = request.getHeader(properties.getTokenHeader());
        if (StringUtils.isEmpty(header)) {
            return "";
        }
        if (!header.startsWith(headerPrefix)) {
            return "";
        }
        return header.substring(headerPrefix.length());
    }

    private String extractFromParameterMap(HttpServletRequest request, AccessTokenConfig properties) {
        String token = null;
        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            String[] tokenParamVal = request.getParameterMap().get(properties.getTokenQueryParam());
            if (tokenParamVal != null && tokenParamVal.length == 1) {
                token = tokenParamVal[0];
            }
        }
        return token;
    }
}
