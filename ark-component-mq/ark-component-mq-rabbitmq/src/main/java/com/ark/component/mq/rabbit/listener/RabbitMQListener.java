package com.ark.component.mq.rabbit.listener;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.MQType;
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
    public void listen(MessageHandler<Envelope> handler,
                       MQListenerConfig listenerConfig) {
        if (!configuration.getEnabled()) {
            log.info("[RabbitMQ]:RabbitMQ组件未启用");
            return;
        }
        String routingKey = listenerConfig.getTag();
        String exchange = listenerConfig.getTopic();
        BuiltinExchangeType exchangeType = convertExchangeType(listenerConfig.getConsumeMode());
        String queue = Utils.createQueueName(exchange, routingKey, exchangeType);
        Connection connection = null;
        Channel channel = null;

        try {
            connection = ConnectionManager.getConnection(configuration);
            channel = connection.createChannel();
            // 声明交换机
            channel.exchangeDeclare(exchange, exchangeType, true, false, false, null);
            // 如果交换机是Fanout类型，队列无须持久话，消费者下线后自动删除
            if (exchangeType.equals(BuiltinExchangeType.FANOUT)) {
                channel.queueDeclare(queue, false, true, true, null);
            } else {
                channel.queueDeclare(queue, true, false, false, null);
            }
            // 队列绑定
            channel.queueBind(queue, exchange, routingKey);
            // 注册消费者
            channel.basicConsume(queue, false, consumer(channel, handler));
        } catch (Exception e) {
            throw new MQListenException(e);
        } finally {
            registryShutdownHook(handler, connection, channel);
        }
    }

    private BuiltinExchangeType convertExchangeType(ConsumeMode consumeMode) {
        if (consumeMode.equals(ConsumeMode.BROADCASTING)) {
            return BuiltinExchangeType.FANOUT;
        } else if (consumeMode.equals(ConsumeMode.CLUSTERING)) {
            return BuiltinExchangeType.TOPIC;
        }
        throw new MQListenException("[RabbitMQ]:Cannot support consume mode");
    }

    private DefaultConsumer consumer(Channel channel, MessageHandler<Envelope> processor) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                long deliveryTag = envelope.getDeliveryTag();
                String messageId = properties.getMessageId();
                if (log.isDebugEnabled()) {
                    log.debug("[RabbitMQ]:Receive message -> msgId=[{}], deliveryTag=[{}], envelope=[{}]", messageId, deliveryTag, envelope);
                }
                Channel channel = getChannel();
                try {
                    processor.handle(body, messageId, envelope);
                    channel.basicAck(deliveryTag, false);
                } catch (MessageRequeueException e) {
                    channel.basicReject(deliveryTag, true);
                    log.info("[RabbitMQ]:Consume Failed, message back to the queue -> msgId=[{}], deliveryTag=[{}], envelope=[{}], err={}", messageId, deliveryTag, envelope, ExceptionUtil.stacktraceToString(e));
                } catch (MQException e) {
                    channel.basicReject(deliveryTag, false);
                    log.info("[RabbitMQ]:Failed to consume, message discarded -> msgId=[{}], deliveryTag=[{}], envelope=[{}], err={}", messageId, deliveryTag, envelope, ExceptionUtil.stacktraceToString(e));
                }
            }
        };
    }

    private void registryShutdownHook(MessageHandler<Envelope> processor, Connection connection, Channel channel) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> close(processor, connection, channel)));
    }

    private static void close(MessageHandler<Envelope> processor, Connection connection, Channel channel) {
        try {
            channel.close();
            connection.close();
            log.info("[RabbitMQ]：消费者：[{}] 停止成功......", processor.getClass());
        } catch (Exception e) {
            log.info("[RabbitMQ]：消费者：[{}] 停止失败......", processor.getClass());
        }
    }

    @Override
    public MQType getMqType() {
        return MQType.RABBIT;
    }
}
