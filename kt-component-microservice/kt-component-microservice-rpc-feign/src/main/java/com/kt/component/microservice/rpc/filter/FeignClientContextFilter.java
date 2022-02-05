
package com.kt.component.microservice.rpc.filter;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

@Slf4j
@ConditionalOnClass({RequestInterceptor.class})
public class FeignClientContextFilter implements RequestInterceptor {

    public FeignClientContextFilter() {
    }

    public void apply(RequestTemplate template) {
        log.warn("请求头内容透传！Url:{},Method={}", template.url(), template.method());
        this.delivery(template);
    }

    public void delivery(RequestTemplate template) {
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
