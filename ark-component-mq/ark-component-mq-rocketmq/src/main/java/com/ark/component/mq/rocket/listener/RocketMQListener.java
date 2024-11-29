package com.ark.component.mq.rocket.listener;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.MQType;
import com.ark.component.mq.exception.MessageRequeueException;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.stream.Collectors;

@Slf4j
public class RocketMQListener implements MQListener<MessageExt> {

    private final RocketMQConfiguration configuration;

    public RocketMQListener(RocketMQConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void listen(MessageHandler<MessageExt> processor,
                       MQListenerConfig listenerConfig) {
        if (!configuration.getEnabled()) {
            log.info("[RocketMQ]:RocketMQ组件未启用");
            return;
        }
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(configuration.getServer());
        consumer.setConsumerGroup(listenerConfig.getConsumerGroup());
        consumer.setConsumeTimeout(listenerConfig.getConsumeTimeout());
        consumer.setConsumeMessageBatchMaxSize(listenerConfig.getConsumeMessageBatchMaxSize());
        consumer.setMaxReconsumeTimes(10);
        try {
            consumer.setMessageModel(convertMessageModel(listenerConfig.getConsumeMode()));
            consumer.subscribe(listenerConfig.getTopic(), listenerConfig.getTag());
            consumer.registerMessageListener(registryMessageListener(processor));
            consumer.start();
        } catch (Exception e) {
            throw new MQListenException(e);
        } finally {
            close(processor, consumer);
            registryShutdownHook(processor, consumer);
        }
    }

    private void registryShutdownHook(MessageHandler<MessageExt> processor, DefaultMQPushConsumer consumer) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> close(processor, consumer)));
    }

    private void close(MessageHandler<MessageExt> processor, DefaultMQPushConsumer consumer) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                consumer.shutdown();
                log.info("Consumer：[{}] shutdown success......", processor.getClass());
            } catch (Exception e) {
                log.info("Consumer：[{}] shutdown error......", processor.getClass(), e);
            }
        }));
    }

    private MessageListenerConcurrently registryMessageListener(MessageHandler<MessageExt> processor) {
        return (msgList, context) -> {
            String msgIds = msgList.stream().map(MessageExt::getMsgId).collect(Collectors.joining(","));
            if (log.isDebugEnabled()) {
                log.debug("[RocketMQ]:Receive message -> msgId=[{}]", msgIds);
            }
            try {
                for (MessageExt messageExt : msgList) {
                    processor.handle(messageExt.getBody(), messageExt.getMsgId(), messageExt);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (MessageRequeueException e) {
                log.error("[RocketMQ]:Consume Failed, message back to the queue -> msgId=[{}], err={}",
                        msgIds, ExceptionUtil.stacktraceToString(e));
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            } catch (MQException e) {
                log.error("[RocketMQ]:Failed to consume, message discarded -> msgId=[{}], err={}",
                        msgIds, ExceptionUtil.stacktraceToString(e));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
    }

    @Override
    public MQType getMqType() {
        return MQType.ROCKET;
    }

    private MessageModel convertMessageModel(ConsumeMode consumeMode) {
        if (consumeMode.equals(ConsumeMode.BROADCASTING)) {
            return MessageModel.BROADCASTING;
        } else if (consumeMode.equals(ConsumeMode.CLUSTERING)) {
            return MessageModel.CLUSTERING;
        }
        throw new MQListenException("[RocketMQ]:Cannot support consume mode");
    }
}
