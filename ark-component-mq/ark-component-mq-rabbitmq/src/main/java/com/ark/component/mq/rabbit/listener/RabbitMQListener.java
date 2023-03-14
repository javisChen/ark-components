package com.ark.component.mq.rabbit.listener;

import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.rabbit.RabbitConsumer;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.ark.component.mq.rabbit.support.ConnectionManager;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.RabbitUtils;

import java.io.IOException;

@Slf4j
public class RabbitMQListener implements MQListener<Envelope> {

    private final RabbitMQConfiguration configuration;

    public RabbitMQListener(RabbitMQConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public void listen(MQMessageProcessor<Envelope> processor,
                       MQListenerConfig listenerConfig) {

        Connection connection = null;

        Channel channel = null;

        try {
            connection = ConnectionManager.getConnection(configuration);
            channel = connection.createChannel();

            String routingKey = listenerConfig.getTag();
            String exchange = listenerConfig.getTopic();
            String queue = buildQueueName(exchange, routingKey);

            // 声明交换机
            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC, true, false, false, null);
            // 声明队列
            channel.queueDeclare(queue, true, false, false, null);
            // 队列绑定
            channel.queueBind(queue, exchange, routingKey);
            // 注册消费者
            channel.basicConsume(queue, new RabbitConsumer(channel, processor));
        } catch (Exception e) {
            throw new MQListenException(e);
        } finally {
            close(processor, connection, channel);
        }
    }

    private void close(MQMessageProcessor<Envelope> processor, Connection connection, Channel channel) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                channel.close();
                connection.close();
                log.info("Consumer：[{}] shutdown success......", processor.getClass());
            } catch (Exception e) {
                log.info("Consumer：[{}] shutdown error......", processor.getClass());
            }
        }));
    }

    private String buildQueueName(String exchange, String queue) {
        return exchange + "_" + queue;
    }

    @Override
    public MQType getMqType() {
        return MQType.RABBIT;
    }
}
