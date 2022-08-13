package com.kt.component.mq.rocket;

import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.AbstractMessageService;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    protected SendResult executeSend(String topic, String tag, Message<MessagePayLoad> message, long timeout, int delayLevel) {
        return rocketMQTemplate.syncSend(buildDestination(topic, tag), message, timeout, delayLevel);
    }

    @Override
    protected Message<MessagePayLoad> buildBody(MessagePayLoad messagePayLoad) {
        return MessageBuilder.withPayload(messagePayLoad).build();
    }

    @Override
    protected void executeAsyncSend(String topic, String tag, Message<MessagePayLoad> message, long timeout, int delayLevel, MessageSendCallback callback) {
        rocketMQTemplate.asyncSend(buildDestination(topic, tag), message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (log.isDebugEnabled()) {
                    log.debug("[mq] start send message msgId:{} topic:{} tag:{} payLoad:{} ",
                            message.getPayload().getMsgId(), topic, tag, JSON.toJSONString(message));
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
