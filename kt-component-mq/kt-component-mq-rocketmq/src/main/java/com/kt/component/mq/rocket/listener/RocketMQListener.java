package com.kt.component.mq.rocket.listener;

import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import com.kt.component.mq.core.support.ConsumeMode;
import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.exception.MQListenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

@Slf4j
public class RocketMQListener implements MQListener {

    @Override
    public void listen(MQMessageProcessor processor,
                       MQConfiguration mqConfiguration,
                       MQListenerConfig listenerConfig) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();
        RocketMQConfiguration rocketMQ = mqConfiguration.getRocketMQ();
        consumer.setNamesrvAddr(rocketMQ.getServer());
        consumer.setConsumerGroup(listenerConfig.getConsumerGroup());
        consumer.setConsumeTimeout(listenerConfig.getConsumeTimeout());
        try {
            consumer.setMessageModel(choiceMessageModel(listenerConfig.getConsumeMode()));
            consumer.subscribe(listenerConfig.getTopic(), listenerConfig.getTag());
            consumer.registerMessageListener((MessageListenerConcurrently) (msgList, context) -> {
                try {
                    for (MessageExt messageExt : msgList) {
                        if (processor.process(messageExt.getMsgId(), messageExt.getBody())) {
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

    private MessageModel choiceMessageModel(ConsumeMode consumeMode) {
        if (consumeMode.equals(ConsumeMode.BROADCASTING)) {
            return MessageModel.BROADCASTING;
        } else if (consumeMode.equals(ConsumeMode.CLUSTERING)) {
            return MessageModel.CLUSTERING;
        }
        throw new MQListenException("cannot support consume mode");
    }
}
