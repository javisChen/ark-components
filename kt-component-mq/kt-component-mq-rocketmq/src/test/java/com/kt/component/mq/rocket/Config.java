package com.kt.component.mq.rocket;

import com.kt.component.mq.MqService;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public MqService mqService(RocketMQTemplate rocketMQTemplate) {
        return new RocketMqService(rocketMQTemplate);
    }
}
