
package com.ark.component.microservice.rpc.filter;


import com.ark.component.microservice.rpc.config.CloudFeignProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Target;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * feign调用把当前请求的header透传到目标服务
 * @author victor
 */
@Slf4j
@Component
public class FeignRpcContextInterceptor implements RequestInterceptor {

    private CloudFeignProperties cloudFeignProperties;

    public FeignRpcContextInterceptor(CloudFeignProperties cloudFeignProperties) {
        this.cloudFeignProperties = cloudFeignProperties;
    }

    public FeignRpcContextInterceptor() {
    }

    public void apply(RequestTemplate template) {
        logRequest(template);
        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {
            String key = entry.getKey();
            String headerValue = entry.getValue();
            if (getCloudFeignConfig().getTransmitHeaders().contains(key)) {
                setHeader(template, key, headerValue);
            }
        }
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


    protected void logRequest(RequestTemplate template) {
        Target<?> target = template.feignTarget();
        log.info("FEIGN REQUEST -> TARGET:[{}]", target.url());
        log.info("FEIGN REQUEST -> CONFIG KEY:[{}]", template.methodMetadata().configKey());
        log.info("FEIGN REQUEST -> HTTP_METHOD:[{}]", template.method());
        log.info("FEIGN REQUEST -> QUERIES:[{}]", template.queryLine());
        log.info("FEIGN REQUEST -> BODY:[{}]", new String(template.body()));
    }

    protected  <T> void setHeader(RequestTemplate template, String header, T value) {
        if (value != null) {
            template.header(header, value.toString());
        }
    }

    public CloudFeignProperties getCloudFeignConfig() {
        return cloudFeignProperties;
    }
}
