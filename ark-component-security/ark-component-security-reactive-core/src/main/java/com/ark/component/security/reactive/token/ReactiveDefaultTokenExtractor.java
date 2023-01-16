package com.ark.component.security.reactive.token;

import cn.hutool.core.collection.CollUtil;
import com.ark.component.security.base.token.AccessTokenProperties;
import com.ark.component.security.base.token.extractor.AccessTokenExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import java.util.List;

/**
 * Token提取器默认实现
 * @author jc
 */
public class ReactiveDefaultTokenExtractor implements AccessTokenExtractor<ServerHttpRequest> {

    private final AccessTokenProperties tokenProperties;

    public ReactiveDefaultTokenExtractor(AccessTokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public String extract(ServerHttpRequest request) {
        String accessToken = extractFromParameterMap(request);
        if (StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        return StringUtils.substringAfter(request.getHeaders().getFirst(tokenProperties.getTokenHeader()), tokenProperties.getTokenHeaderPrefix());
    }

    private String extractFromParameterMap(ServerHttpRequest request) {
        if (CollUtil.isNotEmpty(request.getQueryParams())) {
            List<String> tokenParamVal = request.getQueryParams().get(tokenProperties.getTokenQueryParam());
            if (CollUtil.isNotEmpty(tokenParamVal)) {
                return tokenParamVal.get(0);
            }
        }
        return "";
    }

}
