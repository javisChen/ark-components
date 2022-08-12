package com.kt.component.mq.rocket;

import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.AbstractMessageService;
import com.kt.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
public class RocketMessageService extends AbstractMessageService<Message<MessagePayLoad>, SendResult> {

    private final RocketMQTemplate rocketMQTemplate;

    public RocketMessageService(RocketMQTemplate rocketMQTemplate, MQConfiguration mqConfiguration) {
        super(mqConfiguration);
        this.rocketMQTemplate = rocketMQTemplate;
    }

    private MessageResponse doSend(String topic, String tag, MessagePayLoad payLoad, long timeout, int delayLevel) {
        String destination = StringUtils.isNotBlank(tag) ? buildDestination(topic, tag) : topic;
        SendResult sendResult;
        try {
            Message<MessagePayLoad> message = MessageBuilder.withPayload(payLoad).build();
            if (log.isDebugEnabled()) {
                log.debug("[rocket mq] start send message destination:{} payLoad:{}",
                        destination, JSON.toJSONString(payLoad));
            }

            sendResult = rocketMQTemplate.syncSend(destination, message, timeout, delayLevel);
            if (log.isDebugEnabled()) {
                log.debug("[rocket mq] send message destination:{} payLoad:{}, msgId:{}",
                        destination, JSON.toJSONString(payLoad), sendResult.getMsgId());
            }
        } catch (Exception e) {
            log.error("[rocket mq] send message error", e);
            throw new MQException(e);
        }
        return convertToMQResponse(sendResult);
    }

    @Override
    protected SendResult executeSend(String destination, Message<MessagePayLoad> message, long timeout, int delayLevel) {
        return rocketMQTemplate.syncSend(destination, message, timeout, delayLevel);
    }

    @Override
    protected Message<MessagePayLoad> buildBody(MessagePayLoad body) {
        return MessageBuilder.withPayload(body).build();
    }

    private void doAsyncSend(String topic, String tag, MessagePayLoad payLoad, MessageSendCallback callback, long timeout, int delayLevel) {
        String destination = StringUtils.isNotBlank(tag) ? buildDestination(topic, tag) : topic;
        try {
            Message<MessagePayLoad> message = MessageBuilder.withPayload(payLoad).build();
            if (log.isDebugEnabled()) {
                log.debug("[rocket mq] start send message destination:{} payLoad:{}",
                        destination, JSON.toJSONString(payLoad));
            }
            rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (log.isDebugEnabled()) {
                        log.debug("[rocket mq] send message success destination:{} payLoad:{}, msgId:{}",
                                destination, JSON.toJSONString(payLoad), sendResult.getMsgId());
                    }
                    if (callback != null) {
                        callback.onSuccess(convertToMQResponse(sendResult));
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[rocket mq] send message error callback", throwable);
                    if (callback != null) {
                        callback.onException(throwable);
                    }
                }
            }, timeout, delayLevel);
        } catch (Exception e) {
            log.error("[rocket mq] send message error", e);
            throw new MQException(e);
        }
    }

    protected String buildDestination(String topic, String payLoad) {
        return topic + ":" + payLoad;
    }

    protected MessageResponse convertToMQResponse(SendResult sendResult) {
        return MessageResponse.builder()
                .withMsgId(sendResult.getMsgId())
                .build();
    }

}
