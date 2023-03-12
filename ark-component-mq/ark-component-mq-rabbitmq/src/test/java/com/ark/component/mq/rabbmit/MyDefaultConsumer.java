package com.ark.component.mq.rabbmit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class MyDefaultConsumer extends DefaultConsumer {

    public MyDefaultConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.info("消费消息：consumerTag={}, envelope={}, body={}", consumerTag, envelope, new String(body));
        Channel channel = getChannel();
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
