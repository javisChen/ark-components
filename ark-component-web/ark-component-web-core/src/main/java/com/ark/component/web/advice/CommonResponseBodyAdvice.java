package com.ark.component.web.advice;

import com.ark.component.context.core.ServiceContext;
import com.ark.component.dto.ServerResponse;
import com.ark.component.web.base.BaseController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应BODY统一返回处理，自动追加上traceId和service字段
 * @author jc
 */
@RestControllerAdvice(assignableTypes = BaseController.class)
public class CommonResponseBodyAdvice implements ResponseBodyAdvice<Object>, EnvironmentAware, InitializingBean {

    private Environment environment;

    private String applicationName;

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
                serverResponse.setService(applicationName);
            }
        }
        return body;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() {
        applicationName = environment.getProperty("spring.application.name", String.class);
    }

}
