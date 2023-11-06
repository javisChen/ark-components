
package com.ark.component.microservice.rpc.filter;


import com.ark.component.microservice.rpc.config.CloudFeignProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * feign调用把当前请求的header透传到目标服务
 *
 * @author victor
 */
@Slf4j
public class FeignRpcContextInterceptor implements RequestInterceptor {

    String HEADER_FROM = "X-From";
    String HEADER_FROM_SERVICE = "srv";

    private CloudFeignProperties cloudFeignProperties;

    public FeignRpcContextInterceptor(CloudFeignProperties cloudFeignProperties) {
        this.cloudFeignProperties = cloudFeignProperties;
    }

    public FeignRpcContextInterceptor() {
    }

    @Override
    public void apply(RequestTemplate template) {
        Map<String, String> headers = getHeaders();
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String key = entry.getKey();
                String headerValue = entry.getValue();
                // 跳过 content-length，解决too many bites written的问题
                if (key.equalsIgnoreCase("content-length")){
                    continue;
                }
                setHeader(template, key, headerValue);
            }
        }
        template.header(HEADER_FROM, HEADER_FROM_SERVICE);
    }

    protected Map<String, String> getHeaders() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>(16);
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            headerMap.put(headerName, header);
        }
        return headerMap;
    }

    protected <T> void setHeader(RequestTemplate template, String header, T value) {
        if (value != null) {
            template.header(header, value.toString());
        }
    }

    public CloudFeignProperties getCloudFeignConfig() {
        return cloudFeignProperties;
    }
}
