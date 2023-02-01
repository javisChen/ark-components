package com.ark.component.mq.integration.autoconfigure;

import com.ark.component.mq.MQMessageService;
import com.ark.component.mq.core.producer.MessageProducer;
import com.ark.component.mq.rocket.RocketMQMessageService;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import com.ark.component.mq.rocket.listener.RocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * MQ集成监听装配类
 */
@Slf4j
@EnableConfigurationProperties(RocketMQConfiguration.class)
public class RocketMQAutoConfiguration {

    public RocketMQAutoConfiguration() {
        log.info("enable [ark-component-mq-integration-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public RocketMQListener rocketMQListener(RocketMQConfiguration configuration) {
        return new RocketMQListener(configuration);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "ark.component.mq.rocketmq.producer",
            value = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    @ConditionalOnMissingBean
    public RocketMQMessageService rocketMessageService(RocketMQConfiguration configuration) {
        return new RocketMQMessageService(configuration);
    }

    @Bean
    @ConditionalOnBean(MQMessageService.class)
    @ConditionalOnMissingBean
    public MessageProducer messageProducer(MQMessageService messageService) {
        return new MessageProducer(messageService);
    }

}
