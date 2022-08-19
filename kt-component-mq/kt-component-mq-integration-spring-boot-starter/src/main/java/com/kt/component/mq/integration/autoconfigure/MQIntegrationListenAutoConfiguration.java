package com.kt.component.mq.integration.autoconfigure;

import com.kt.component.mq.integration.MQListenStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * MQ集成监听装配类
 */
@Slf4j
public class MQIntegrationListenAutoConfiguration {

    public MQIntegrationListenAutoConfiguration() {
        log.info("enable [kt-component-mq-integration-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public MQListenStarter mqListenStarter() {
        return new MQListenStarter();
    }

}
