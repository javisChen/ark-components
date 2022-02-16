package com.kt.component.exception.autoconfigure;

import com.kt.component.exception.controller.CommonBasicErrorController;
import com.kt.component.exception.handler.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
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

    private ServerProperties serverProperties;

    public ExceptionAutoConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public ExceptionAutoConfiguration() {
        log.info("enable [kt-component-exception-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public CommonBasicErrorController basicErrorController(ErrorAttributes errorAttributes,
                                                    ServerProperties serverProperties,
                                                    ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {

        return new CommonBasicErrorController(errorAttributes, serverProperties.getError(),
                errorViewResolversProvider.getIfAvailable());
    }
}
