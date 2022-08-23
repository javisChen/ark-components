package com.kt.component.mq.rocket;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.core.AbstractMQMessageService;
import com.kt.component.mq.exception.MQException;
import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

@Slf4j
public class RocketMQMessageService extends AbstractMQMessageService<Message, SendResult> {

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
    protected SendResult executeSend(String topic, String tag, Message message, long timeout, int delayLevel) {
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
                                    Message message,
                                    long timeout,
                                    int delayLevel,
                                    MessageSendCallback callback,
                                    String msgId) {
        try {
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (callback != null) {
                        callback.onSuccess(convertToMQResponse(sendResult));
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

    protected MessageResponse convertToMQResponse(SendResult sendResult) {
        return MessageResponse.builder()
                .withMsgId(sendResult.getMsgId())
                .build();
    }

    @Override
    protected Message buildMessage(String topic, String tag, int delayLevel, MessagePayLoad messagePayLoad) {
        Message message = new Message(topic, tag, messagePayLoad.getMsgId(), JSONObject.toJSONBytes(messagePayLoad));
        message.setDelayTimeLevel(delayLevel);
        return message;
    }

}
