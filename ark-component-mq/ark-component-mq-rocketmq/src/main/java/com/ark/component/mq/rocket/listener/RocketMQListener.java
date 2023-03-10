package com.ark.component.mq.rocket.listener;

import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.exception.MQListenException;
import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.Map;

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
        try {
            consumer.setMessageModel(choiceMessageModel(listenerConfig.getConsumeMode()));
            consumer.subscribe(listenerConfig.getTopic(), listenerConfig.getTag());
            consumer.registerMessageListener(registryMessageListener(processor));
            consumer.start();
        } catch (MQClientException e) {
            throw new MQListenException(e);
        } finally {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    consumer.shutdown();
                    log.info("Consumer：[{}] shutdown success......", processor.getClass());
                } catch (Exception e) {
                    log.info("Consumer：[{}] shutdown error......", processor.getClass());
                }
            }));
        }
    }

    private MessageListenerConcurrently registryMessageListener(MQMessageProcessor<MessageExt> processor) {
        return (msgList, context) -> {
            try {
                for (MessageExt messageExt : msgList) {
                    processor.process(messageExt.getBody(), messageExt.getMsgId(), messageExt);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
                log.error("MQ Process Error -> ", e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        };
    }

    @Override
    public MQType mqType() {
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
