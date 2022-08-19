package com.kt.component.mq.rocket;

import com.kt.component.mq.MessageService;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Config {

    @Bean
    public MQConfiguration mqConfiguration() {
        MQConfiguration mqConfiguration = new MQConfiguration();
        RocketMQConfiguration rocketMQ = new RocketMQConfiguration();
        rocketMQ.setServer("localhost:9876");
        RocketMQConfiguration.Producer producer = new RocketMQConfiguration.Producer();
        producer.setSendMessageTimeout(3000);
        producer.setGroup("pg_test");
        rocketMQ.setProducer(producer);
        mqConfiguration.setRocketMQ(rocketMQ);
        return mqConfiguration;
    }

    @Bean
    public MessageService mqService(MQConfiguration mqConfiguration) {
        return new RocketMessageService(mqConfiguration);
    }

//    @Bean
//    public MessageService mqService(RocketMQTemplate rocketMQTemplate, MQConfiguration mqConfiguration) {
//        return new RocketMessageService(mqConfiguration);
//    }

}
