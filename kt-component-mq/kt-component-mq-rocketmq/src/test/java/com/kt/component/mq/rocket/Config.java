package com.kt.component.mq.rocket;

import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageService;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.configuation.MQConfiguration.Producer;
import com.kt.component.mq.core.MQListener;
import com.kt.component.mq.core.consumer.MQMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Config {

    @Bean
    public MQConfiguration mqConfiguration() {
        MQConfiguration mqConfiguration = new MQConfiguration();
        mqConfiguration.setServer("localhost:9876");
        Producer producer = new Producer();
        producer.setSendMessageTimeout(3000);
        producer.setGroup("pg_test");
        mqConfiguration.setProducer(producer);
        return mqConfiguration;
    }

    @Bean
    public MessageService mqService(MQConfiguration mqConfiguration) {
        return new RocketMessageService(mqConfiguration);
    }

    @Bean
    public MQListener mqListener(MQConfiguration mqConfiguration) {
        return new RocketMQListener("test", "", new MQMessageProcessor<MessagePayLoad>() {
            @Override
            protected void handleMessage(MessagePayLoad message) {
                log.info("consume msg -> {}", JSON.toJSONString(message));
            }
        }, mqConfiguration);
    }

//    @Bean
//    public MessageService mqService(RocketMQTemplate rocketMQTemplate, MQConfiguration mqConfiguration) {
//        return new RocketMessageService(mqConfiguration);
//    }

}
