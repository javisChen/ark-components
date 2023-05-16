package com.ark.component.mq.rabbit;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class DefaultReturnCallback implements RabbitTemplate.ReturnsCallback {

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {

    }
}
