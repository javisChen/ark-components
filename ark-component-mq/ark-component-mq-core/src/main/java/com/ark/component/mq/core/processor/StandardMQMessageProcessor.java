package com.ark.component.mq.core.processor;

import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSON;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.core.serializer.MessageCodec;
import com.ark.component.mq.exception.MQCodecException;
import com.ark.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 一个处理消息的框架。极大多数情况，只需要继承当前Processor
 * 1.消息反序列化
 * 2.消息幂等校验
 * 3.消息处理
 * @param <T>
 */
@Slf4j
@SuppressWarnings("all")
public abstract class StandardMQMessageProcessor<T, RAW> implements MQMessageProcessor<RAW>, ApplicationContextAware {

    private MessageCodec messageCodec;

    @Override
    public void process(byte[] body, String msgId, RAW raw) throws MQException {
        log.info("[MQ] Consume Message MsgId = {}, BodySize = {}", msgId, body.length);
        // 反序列化
        MsgBody message;
        T msgBody;
        try {
            message = messageCodec.decode(body, MsgBody.class);
            log.info("[MQ] Consume Message MsgId = {}, Decode = {}", msgId, JSON.toJSONString(message));
            msgBody = convertMsgBody(message);
        } catch (MQCodecException e) {
            log.error("[MQ] Consume Message Decode Error MsgId = " + msgId, e);
            throw new MQCodecException(e);
        }
        String sendId = message.getBizKey();
        try {
            // 消费幂等校验
            if (isRepeatMessage(msgId, sendId, msgBody, raw)) {
                if (log.isDebugEnabled()) {
                    log.debug("[MQ] Message Already Consume MsgId = {}", msgId);
                }
                return;
            }
            handleMessage(msgId, sendId, msgBody, raw);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] Message Consume Success");
            }
        } catch (Exception e) {
            log.error("[MQ] Message Consume Handle Rrror", e);
            throw new MQException(e);
        }
    }

    private T convertMsgBody(MsgBody msgBody) {
        return JSON.parseObject(JSON.toJSONString(msgBody.getBody()), TypeUtil.getTypeArgument(getClass()));
    }

    protected abstract void handleMessage(String msgId, String sendId, T body, RAW raw);

    protected boolean isRepeatMessage(String msgId, String sendId, T body, RAW raw) {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setMessageCodec(applicationContext.getBean(MessageCodec.class));
    }

    public void setMessageCodec(MessageCodec messageCodec) {
        this.messageCodec = messageCodec;
    }
}
