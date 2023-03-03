package com.ark.component.mq.integration.autoconfigure;

import com.ark.component.mq.MQService;
import com.ark.component.mq.core.producer.MessageProducer;
import com.ark.component.mq.rocket.RocketMQService;
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
    public RocketMQService rocketMessageService(RocketMQConfiguration configuration) {
        return new RocketMQService(configuration);
    }

    @Bean
    @ConditionalOnBean(MQService.class)
    @ConditionalOnMissingBean
    public MessageProducer messageProducer(MQService messageService) {
        return new MessageProducer(messageService);
    }

}
