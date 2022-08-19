package com.kt.component.mq.integration.autoconfigure;

import com.kt.component.mq.rocket.RocketMQMessageService;
import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import com.kt.component.mq.rocket.listener.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * MQ集成监听装配类
 */
@Slf4j
@EnableConfigurationProperties(RocketMQConfiguration.class)
public class RocketMQAutoConfiguration {

    public RocketMQAutoConfiguration() {
        log.info("enable [kt-component-mq-integration-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public RocketMQListener rocketMQListener(RocketMQConfiguration configuration) {
        return new RocketMQListener(configuration);
    }

    @Bean
    @ConditionalOnMissingBean
    public RocketMQMessageService rocketMessageService(RocketMQConfiguration configuration) {
        return new RocketMQMessageService(configuration);
    }

}
