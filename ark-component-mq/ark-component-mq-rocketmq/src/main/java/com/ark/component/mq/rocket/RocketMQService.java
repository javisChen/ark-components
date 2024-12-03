package com.ark.component.mq.rocket;

import com.alibaba.fastjson2.JSON;
import com.ark.component.mq.MQType;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.SendConfirm;
import com.ark.component.mq.SendResult;
import com.ark.component.mq.core.AbstractMQService;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.util.Assert;

@Slf4j
public class RocketMQService extends AbstractMQService<Message, org.apache.rocketmq.client.producer.SendResult> {
    private DefaultMQProducer defaultMQProducer;
    private RocketMQTemplate rocketMQTemplate;

    public RocketMQService(RocketMQConfiguration mqConfiguration) {
        super(mqConfiguration);
        initProducer(mqConfiguration);
    }

    private void initProducer(RocketMQConfiguration rocketMQConfig) {
        RocketMQConfiguration.Producer producer = rocketMQConfig.getProducer();
        Assert.notNull(producer, "producer must not be null");
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
    protected org.apache.rocketmq.client.producer.SendResult executeSend(String bizKey, String topic, String tag, Message msgBody, long timeout, int delayLevel) {
        try {
            msgBody.setDelayTimeLevel(delayLevel);
            return defaultMQProducer.send(msgBody, timeout);
        } catch (Exception e) {
            throw new MQException(e);
        }
    }

    @Override
    protected void executeAsyncSend(String bizKey,
                                    String topic,
                                    String tag,
                                    Message message,
                                    long timeout,
                                    int delayLevel,
                                    SendConfirm callback) {
        try {
            defaultMQProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                    if (callback != null) {
                        SendResult response = SendResult.builder()
                                .withMsgId(sendResult.getMsgId())
                                .withBizKey(bizKey)
                                .build();
                        callback.onSuccess(response);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    if (callback != null) {
                        SendResult response = SendResult.builder()
                                .withBizKey(bizKey)
                                .withNote(throwable.getMessage())
                                .withThrowable(throwable)
                                .build();
                        callback.onException(response);
                    }
                }
            }, timeout);
        } catch (Exception e) {
            log.error("[RocketMQ]:send message error", e);
            throw new MQException(e);
        }
    }

    @Override
    protected SendResult toResponse(org.apache.rocketmq.client.producer.SendResult sendResult, String bizKey) {
        return SendResult.builder()
                .withMsgId(sendResult.getMsgId())
                .withBizKey(bizKey)
                .build();
    }

    @Override
    protected Message buildMessage(String topic, String tag, int delayLevel, MsgBody msgBodyPayLoad) {
        Message message = new Message(topic, tag, msgBodyPayLoad.getBizKey(), JSON.toJSONBytes(msgBodyPayLoad));
        message.setDelayTimeLevel(delayLevel);
        return message;
    }

    @Override
    public MQType mqType() {
        return MQType.ROCKET;
    }
}
