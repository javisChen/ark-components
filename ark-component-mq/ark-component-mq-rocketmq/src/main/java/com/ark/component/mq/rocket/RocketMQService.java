package com.ark.component.mq.rocket;

import com.alibaba.fastjson.JSONObject;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.MQSendResponse;
import com.ark.component.mq.MQSendCallback;
import com.ark.component.mq.core.AbstractMQService;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

@Slf4j
public class RocketMQService extends AbstractMQService<Message, SendResult> {
    private DefaultMQProducer defaultMQProducer;
    public RocketMQService(RocketMQConfiguration mqConfiguration) {
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
                                    MQSendCallback callback,
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

    protected MQSendResponse convertToMQResponse(SendResult sendResult, String sendId) {
        return MQSendResponse.builder()
                .withSendId(sendId)
                .withMsgId(sendResult.getMsgId())
                .build();
    }

    @Override
    protected Message buildMessage(String topic, String tag, int delayLevel, MsgBody msgBodyPayLoad) {
        Message message = new Message(topic, tag, msgBodyPayLoad.getBizKey(), JSONObject.toJSONBytes(msgBodyPayLoad));
        message.setDelayTimeLevel(delayLevel);
        return message;
    }

}
