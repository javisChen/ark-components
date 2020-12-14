package com.kt.component.web.advice;

import cn.hutool.core.util.ReflectUtil;
import com.kt.component.dto.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 开发背景：
 * kt项目的分页接口全部通过PageRequest<T>入参，入参实例
 * {
 * "current": 1,
 * "size": 10,
 * "params": {
 * <p>
 * }
 * }
 * 为了避免调用方漏传params，导致PageRequest.params为null而造成程序报错，所以当params为null时，默认创建一个新的实例
 *
 * @author Javis
 */
@ControllerAdvice
@Slf4j
public class PageRequestRequestBodyAdvice implements RequestBodyAdvice, InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return targetType.getClass().isAssignableFrom(ParameterizedTypeImpl.class) &&
            ((ParameterizedTypeImpl) targetType).getRawType().isAssignableFrom(PageRequest.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {

        PageRequest pageRequest = (PageRequest) body;
        if (pageRequest.getParams() != null) {
            return body;
        }
        Object params = createTempInstance((ParameterizedTypeImpl) targetType);
        pageRequest.setParams(params);
        return pageRequest;
    }

    private Object createTempInstance(ParameterizedTypeImpl targetType) {
        Type[] actualTypeArguments = targetType.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];
        return ReflectUtil.newInstance(actualTypeArgument.getTypeName());
    }

    /**
     * @title 无请求时的处理
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}