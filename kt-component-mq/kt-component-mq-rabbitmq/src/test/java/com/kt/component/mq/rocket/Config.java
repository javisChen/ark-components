package com.kt.component.mq.rocket;

import com.kt.component.mq.MessageService;
import com.kt.component.mq.rabbit.RabbitMessageService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MessageService mqService(RocketMQTemplate rocketMQTemplate) {
        return new RabbitMessageService(rocketMQTemplate, mqConfiguration);
    }
}
