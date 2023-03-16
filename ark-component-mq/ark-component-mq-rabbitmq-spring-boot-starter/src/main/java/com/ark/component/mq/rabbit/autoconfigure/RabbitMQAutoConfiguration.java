package com.ark.component.mq.rabbit.autoconfigure;

import com.ark.component.mq.MQService;
import com.ark.component.mq.core.producer.MessageProducer;
import com.ark.component.mq.rabbit.RabbitMQService;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.ark.component.mq.rabbit.listener.RabbitMQListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * MQ集成监听装配类
 */
@Slf4j
@EnableConfigurationProperties(RabbitMQConfiguration.class)
public class RabbitMQAutoConfiguration {

    public RabbitMQAutoConfiguration() {
        log.info("enable [ark-component-mq-integration-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitMQListener rabbitMqListener(RabbitMQConfiguration configuration) {
        return new RabbitMQListener(configuration);
    }

    @Bean
//    @ConditionalOnProperty(
//            prefix = "ark.component.mq.rocketmq.producer",
//            value = "enabled",
//            havingValue = "true",
//            matchIfMissing = true)
    @ConditionalOnMissingBean
    public RabbitMQService rabbitMQService(RabbitMQConfiguration configuration) {
        return new RabbitMQService(configuration);
    }

//    @Bean
//    @ConditionalOnBean(RabbitMQService.class)
//    @ConditionalOnMissingBean
//    public MessageProducer messageProducer(RabbitMQService rabbitMQService) {
//        return new MessageProducer(rabbitMQService);
//    }

}
