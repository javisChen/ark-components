package com.kt.component.mq.rocket.listener;

import com.kt.component.mq.exception.MQListenException;
import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.support.ConsumeMode;
import com.kt.component.mq.core.support.MQType;
import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

@Slf4j
public class RocketMQListener implements MQListener<MessageExt> {

    private RocketMQConfiguration configuration;

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
        try {
            consumer.setMessageModel(choiceMessageModel(listenerConfig.getConsumeMode()));
            consumer.subscribe(listenerConfig.getTopic(), listenerConfig.getTag());
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                try {
                    for (MessageExt messageExt : msgList) {
                        if (processor.process(messageExt.getMsgId(), messageExt.getBody(), messageExt)) {
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            });
            consumer.start();
        } catch (MQClientException e) {
            throw new MQListenException(e);
        }
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
        throw new MQListenException("cannot support consume mode");
    }
}
