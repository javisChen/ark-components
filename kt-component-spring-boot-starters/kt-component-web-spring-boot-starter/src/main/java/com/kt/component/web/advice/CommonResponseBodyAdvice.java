package com.kt.component.web.advice;

import com.kt.component.common.util.spring.SpringUtils;
import com.kt.component.context.ServiceContext;
import com.kt.component.dto.ServerResponse;
import com.kt.component.web.base.BaseController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一
 */
@RestControllerAdvice(assignableTypes = BaseController.class)
public class CommonResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null) {
            if (ServerResponse.class.isAssignableFrom(body.getClass())) {
                ServerResponse serverResponse = (ServerResponse) body;
                serverResponse.setTraceId(ServiceContext.getTraceId());
                serverResponse.setService(SpringUtils.getApplicationName());
            }
        }
        return body;
    }
}
