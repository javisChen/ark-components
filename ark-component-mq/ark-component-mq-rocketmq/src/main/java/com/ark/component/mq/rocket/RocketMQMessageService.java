package com.ark.component.mq.rocket;

import com.alibaba.fastjson.JSONObject;
import com.ark.component.mq.Message;
import com.ark.component.mq.MessageResponse;
import com.ark.component.mq.MessageSendCallback;
import com.ark.component.mq.core.AbstractMQMessageService;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

@Slf4j
public class RocketMQMessageService extends AbstractMQMessageService<org.apache.rocketmq.common.message.Message, SendResult> {
    private DefaultMQProducer defaultMQProducer;
    public RocketMQMessageService(RocketMQConfiguration mqConfiguration) {
        super(mqConfiguration);
        initProducer(mqConfiguration);
    }

    private void initProducer(RocketMQConfiguration rocketMQConfig) {
        RocketMQConfiguration.Producer producer = rocketMQConfig.getProducer();
        this.defaultMQProducer = new DefaultMQProducer();
        this.defaultMQProducer.setProducerGroup(producer.getGroup());
        this.defaultMQProducer.setNamesrvAddr(rocketMQConfig.getServer());
        try {
            this.defaultMQProducer.start();
        } catch (MQClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected SendResult executeSend(String topic, String tag, org.apache.rocketmq.common.message.Message message, long timeout, int delayLevel) {
        try {
            message.setDelayTimeLevel(delayLevel);
            return defaultMQProducer.send(message, timeout);
        } catch (Exception e) {
            throw new MQException(e);
        }
    }

    @Override
    protected void executeAsyncSend(String topic,
                                    String tag,
                                    org.apache.rocketmq.common.message.Message message,
                                    long timeout,
                                    int delayLevel,
                                    MessageSendCallback callback,
                                    String sendId) {
        try {
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (callback != null) {
                        callback.onSuccess(convertToMQResponse(sendResult, sendId));
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    if (callback != null) {
                        callback.onException(throwable);
                    }
                }
            }, timeout);
        } catch (Exception e) {
            log.error("[rocket mq] send message error", e);
            throw new MQException(e);
        }
    }

    protected MessageResponse convertToMQResponse(SendResult sendResult, String sendId) {
        return MessageResponse.builder()
                .withSendId(sendId)
                .withMsgId(sendResult.getMsgId())
                .build();
    }

    @Override
    protected org.apache.rocketmq.common.message.Message buildMessage(String topic, String tag, int delayLevel, Message messagePayLoad) {
        org.apache.rocketmq.common.message.Message message
                = new org.apache.rocketmq.common.message.Message(topic, tag, messagePayLoad.getSendId(), JSONObject.toJSONBytes(messagePayLoad));
        message.setDelayTimeLevel(delayLevel);
        return message;
    }

}