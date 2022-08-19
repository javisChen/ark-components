package com.kt.component.mq.rocket;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.core.AbstractMessageService;
import com.kt.component.mq.exception.MQException;
import com.kt.component.mq.rocket.configuation.RocketMQConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

@Slf4j
public class RocketMessageService extends AbstractMessageService<Message, SendResult> {

    private RocketMQTemplate rocketMQTemplate;
    private DefaultMQProducer defaultMQProducer;

    public RocketMessageService(RocketMQTemplate rocketMQTemplate, RocketMQConfiguration mqConfiguration) {
        super(mqConfiguration);
        this.rocketMQTemplate = rocketMQTemplate;
        initProducer(mqConfiguration);
    }
    public RocketMessageService(RocketMQConfiguration mqConfiguration) {
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
                                    MessageSendCallback callback) {
        defaultMQProducer.send(message, new SendCallback() {
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
        }, timeout);
    }

    protected MessageResponse convertToMQResponse(SendResult sendResult) {
        return MessageResponse.builder()
                .withMsgId(sendResult.getMsgId())
                .build();
    }

    @Override
    protected Message buildBody(String topic, String tag, int delayLevel, MessagePayLoad messagePayLoad) {
        Message message = new Message(topic, tag, messagePayLoad.getMsgId(), JSONObject.toJSONBytes(messagePayLoad));
        message.setDelayTimeLevel(delayLevel);
        return message;
    }

}
