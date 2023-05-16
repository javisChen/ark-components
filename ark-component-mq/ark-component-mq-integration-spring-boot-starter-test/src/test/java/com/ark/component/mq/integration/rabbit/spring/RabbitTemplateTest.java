package com.ark.component.mq.integration.rabbit.spring;

import com.ark.component.mq.integration.ApplicationTests;
import com.ark.component.mq.integration.MQTestConst;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;

public class RabbitTemplateTest extends ApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
//        String tagOrderCreated = MQTestConst.TAG_ORDER_CREATED;
        String tagOrderCreated = "order_order_created_001";
        rabbitTemplate.send(MQTestConst.TOPIC_ORDER, tagOrderCreated, new Message("hello".getBytes(StandardCharsets.UTF_8)));
        rabbitTemplate.send(MQTestConst.TOPIC_ORDER, tagOrderCreated, new Message("hello".getBytes(StandardCharsets.UTF_8)));

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
