package com.kt.component.mq.rocket;

import com.kt.component.mq.MqService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Date;

public class RocketMqService implements MqService {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketMqService(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Override
    public void send(String topic, Object msg) {
        rocketMQTemplate.convertAndSend(topic, msg);
    }

    @Override
    public void asyncSend(String topic, Object msg) {
        System.out.println(new Date() + "提交延迟队列");
        rocketMQTemplate.asyncSend(topic,
                MessageBuilder.withPayload(msg).build(),
                new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(new Date() + " 延迟队列发送成功");
            }

            @Override
            public void onException(Throwable e) {

            }
        }, 1000L, 2);
    }
}
