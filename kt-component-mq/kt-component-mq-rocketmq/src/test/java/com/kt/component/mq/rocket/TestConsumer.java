package com.kt.component.mq.rocket;

import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RocketMQMessageListener(
        messageModel = MessageModel.BROADCASTING,
        consumerGroup = "default_group123",
        topic = "test")
public class TestConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println(new Date() + " message ->" + message);
    }
}