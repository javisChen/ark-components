package com.ark.component.mq.rocket.listener;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.MessageDiscardException;
import com.ark.component.mq.exception.MessageRequeueException;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RocketMQListener implements MQListener<MessageExt> {

    private final RocketMQConfiguration configuration;

    public RocketMQListener(RocketMQConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void listen(MQMessageProcessor<MessageExt> processor,
                       MQListenerConfig listenerConfig) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        consumer.setNamesrvAddr(configuration.getServer());
        consumer.setConsumerGroup(listenerConfig.getConsumerGroup());
        consumer.setConsumeTimeout(listenerConfig.getConsumeTimeout());
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.setMaxReconsumeTimes(10);
        try {
            consumer.setMessageModel(choiceMessageModel(listenerConfig.getConsumeMode()));
            consumer.subscribe(listenerConfig.getTopic(), listenerConfig.getTag());
            consumer.registerMessageListener(registryMessageListener(processor));
            consumer.start();
        } catch (MQClientException e) {
            throw new MQListenException(e);
        } finally {
            close(processor, consumer);
        }
    }

    private void close(MQMessageProcessor<MessageExt> processor, DefaultMQPushConsumer consumer) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                consumer.shutdown();
                log.info("Consumer：[{}] shutdown success......", processor.getClass());
            } catch (Exception e) {
                log.info("Consumer：[{}] shutdown error......", processor.getClass());
            }
        }));
    }

    private MessageListenerConcurrently registryMessageListener(MQMessageProcessor<MessageExt> processor) {
        return (msgList, context) -> {
            String msgIds = msgList.stream().map(MessageExt::getMsgId).collect(Collectors.joining(","));
            log.info("[RocketMQ]接收消息 -> msgId=[{}]", msgIds);
            try {
                for (MessageExt messageExt : msgList) {
                    processor.process(messageExt.getBody(), messageExt.getMsgId(), messageExt);
                }
                log.info("[RocketMQ]消费成功 -> msgId=[{}]", msgIds);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (MessageRequeueException e) {
                log.error("[RocketMQ]消费失败,重新放回队列 -> msgId=[{}], err={}",
                        msgIds, ExceptionUtil.stacktraceToString(e));
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            } catch (MQException e) {
                log.error("[RocketMQ]消费失败,丢弃消息 -> msgId=[{}], err={}",
                        msgIds, ExceptionUtil.stacktraceToString(e));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        };
    }

    @Override
    public MQType getMqType() {
        return MQType.ROCKET;
    }

    private MessageModel choiceMessageModel(ConsumeMode consumeMode) {
        if (consumeMode.equals(ConsumeMode.BROADCASTING)) {
            return MessageModel.BROADCASTING;
        } else if (consumeMode.equals(ConsumeMode.CLUSTERING)) {
            return MessageModel.CLUSTERING;
        }
        throw new MQListenException("Cannot Support Consume Mode");
    }
}
