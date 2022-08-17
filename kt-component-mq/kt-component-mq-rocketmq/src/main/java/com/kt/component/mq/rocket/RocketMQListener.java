package com.kt.component.mq.rocket;

import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.MQListener;
import com.kt.component.mq.core.consumer.MQMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

@Slf4j
public class RocketMQListener implements MQListener {

    private final MQMessageProcessor<MessagePayLoad> messageProcessor;
    private MQConfiguration mqConfiguration;

    private String topic;

    private String tags;

    public RocketMQListener(String topic, String tags, MQMessageProcessor<MessagePayLoad> messageProcessor, MQConfiguration mqConfiguration) {
        this.messageProcessor = messageProcessor;
        this.mqConfiguration = mqConfiguration;
        this.topic = topic;
        this.tags = tags;
        register();
    }

    @Override
    public void register() {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setNamesrvAddr(mqConfiguration.getServer());
        try {
            defaultMQPushConsumer.setConsumerGroup("cp_test");
            defaultMQPushConsumer.subscribe(topic, tags);
            //设置广播消费模式
            defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                try {
                    for (MessageExt messageExt : list) {
                        messageProcessor.process(messageExt.getMsgId(), messageExt.getBody());
                    }
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        try {
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }
}
