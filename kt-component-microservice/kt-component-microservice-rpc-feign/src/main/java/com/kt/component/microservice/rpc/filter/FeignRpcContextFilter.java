
package com.kt.component.microservice.rpc.filter;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign调用把当前请求的header透传到目标服务
 * @author victor
 */
@Slf4j
@ConditionalOnClass({RequestInterceptor.class})
public class FeignRpcContextFilter implements RequestInterceptor {

    public FeignRpcContextFilter() {

    }

    public void apply(RequestTemplate template) {
        log.warn("请求头内容透传！Url:{},Method={}", template.url(), template.method());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            setHeader(template, headerName, header);
        }
    }

    private <T> void setHeader(RequestTemplate template, String header, T value) {
        if (value != null) {
            template.header(header, value.toString());
        }
    }
}
