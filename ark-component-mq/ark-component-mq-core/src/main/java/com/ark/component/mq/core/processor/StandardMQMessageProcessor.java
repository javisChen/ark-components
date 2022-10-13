package com.ark.component.mq.core.processor;

import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSON;
import com.ark.component.mq.Message;
import com.ark.component.mq.core.serializer.MessageCodec;
import com.ark.component.mq.exception.MQDecodeException;
import com.ark.component.mq.exception.MQException;
import lombok.Data;
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

    @Data
    public static class User {
        String name;
    }
    private MessageCodec messageCodec;

    @Override
    public boolean process(byte[] body, String msgId, RAW raw) {
        log.info("[MQ] Consume Message msgId = {}, bodySize = {}", msgId, body.length);
        // 反序列化
        Message message;
        T msgBody;
        try {
            message = messageCodec.decode(body, Message.class);
            log.info("[MQ] Consume Message msgId = {}, decode = {}", msgId, JSON.toJSONString(message));
            msgBody = convertMsgBody(message);
        } catch (Exception e) {
            log.error("[MQ] Consume Message decode error msgId = " + msgId, e);
            throw new MQDecodeException(e);
        }
        String sendId = message.getSendId();
        try {
            // 消费幂等校验
            if (isRepeatMessage(msgId, sendId, msgBody, raw)) {
                if (log.isDebugEnabled()) {
                    log.debug("[MQ] Message already consume msgId = {}", msgId);
                }
                return true;
            }
            handleMessage(msgId, sendId, msgBody, raw);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] Message consume success");
            }
        } catch (Exception e) {
            log.error("[MQ] Message consume handle error", e);
            throw new MQException(e);
        }
        return true;
    }

    private T convertMsgBody(Message message) {
        return JSON.parseObject(JSON.toJSONString(message.getBody()), TypeUtil.getTypeArgument(getClass()));
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
