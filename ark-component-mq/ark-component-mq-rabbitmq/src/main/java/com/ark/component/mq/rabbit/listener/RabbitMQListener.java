package com.ark.component.mq.rabbit.listener;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.exception.MessageRequeueException;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.ark.component.mq.rabbit.support.ConnectionManager;
import com.ark.component.mq.rabbit.support.Utils;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

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
            String queue = Utils.buildQueueName(exchange, routingKey);

            // 声明交换机
            channel.exchangeDeclare(exchange, BuiltinExchangeType.TOPIC, true, false, false, null);
            // 声明队列
            channel.queueDeclare(queue, true, false, false, null);
            // 队列绑定
            channel.queueBind(queue, exchange, routingKey);
            // 注册消费者
            channel.basicConsume(queue, getConsumer(channel, processor));
        } catch (Exception e) {
            throw new MQListenException(e);
        } finally {
            registyShutdownHook(processor, connection, channel);
        }
    }

    private DefaultConsumer getConsumer(Channel channel, MQMessageProcessor<Envelope> processor) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                String messageId = properties.getMessageId();
                log.info("[RabbitMQ]接收消息 -> msgId=[{}], tag=[{}], envelope=[{}]", messageId, deliveryTag, envelope);
                Channel channel = getChannel();
                try {
                    processor.process(body, messageId, envelope);
                    channel.basicAck(deliveryTag, false);
                    log.info("[RabbitMQ]消费成功 -> msgId=[{}], tag=[{}], envelope=[{}]",
                            messageId, deliveryTag, envelope);
                } catch (MessageRequeueException e) {
                    channel.basicReject(deliveryTag, true);
                    log.info("[RabbitMQ]消费失败，重新放回队列 -> msgId=[{}], tag=[{}], envelope=[{}], err={}",
                            messageId, deliveryTag, envelope, ExceptionUtil.stacktraceToString(e));
                } catch (MQException e) {
                    channel.basicReject(deliveryTag, false);
                    log.info("[RabbitMQ]消费失败,丢弃消息 -> msgId=[{}], tag=[{}], envelope=[{}], err={}",
                            messageId, deliveryTag, envelope, ExceptionUtil.stacktraceToString(e));
                }
            }
        };
    }

    private void registyShutdownHook(MQMessageProcessor<Envelope> processor, Connection connection, Channel channel) {
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


    @Override
    public MQType getMqType() {
        return MQType.RABBIT;
    }
}
