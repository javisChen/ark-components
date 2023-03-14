package com.ark.component.mq.rabbit;

import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class RabbitConsumer extends DefaultConsumer {

    private final MQMessageProcessor<Envelope> processor;

    public RabbitConsumer(Channel channel, MQMessageProcessor<Envelope> processor) {
        super(channel);
        this.processor = processor;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        processor.process(body, properties.getMessageId(), envelope);
    }
}
