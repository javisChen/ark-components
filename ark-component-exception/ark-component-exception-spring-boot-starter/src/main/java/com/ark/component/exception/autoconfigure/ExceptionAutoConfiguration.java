package com.ark.component.exception.autoconfigure;

import com.ark.component.exception.controller.CommonBasicErrorController;
import com.ark.component.exception.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
public class ExceptionAutoConfiguration {

    public ExceptionAutoConfiguration() {
        log.info("enable [ark-component-exception-spring-boot-starter]");
    }

    /**
     * 全局统一异常处理Handler
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    /**
     * 覆盖默认的ErrorController，最主要是重写404时的实现逻辑
     */
    @Bean
    public CommonBasicErrorController basicErrorController(ErrorAttributes errorAttributes,
                                                    ServerProperties serverProperties,
                                                    ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {

        return new CommonBasicErrorController(errorAttributes, serverProperties.getError(),
                errorViewResolversProvider.getIfAvailable());
    }

//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        return new MethodValidationPostProcessor();
//    }
}
