package com.ark.component.mq.rabbmit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;

@Slf4j
public class MyDefaultConsumer extends DefaultConsumer {

    public MyDefaultConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        log.info("消费消息：consumerTag={}, envelope={}, body={}, properties={}", consumerTag, envelope, new String(body), properties);
        Channel channel = getChannel();
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
