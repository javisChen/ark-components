package com.kt.component.mq.core.processor;

import cn.hutool.core.util.TypeUtil;
import com.kt.component.mq.core.serializer.MessageCodec;
import com.kt.component.mq.exception.MQException;
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
public abstract class StandardMQMessageProcessor<T, RAW> implements MQMessageProcessor<RAW>, ApplicationContextAware {

    private MessageCodec messageCodec;

    @Override
    public boolean process(String msgId, byte[] body, RAW raw) {
        // 反序列化
        T message = null;
        try {
            message = messageCodec.decode(body, TypeUtil.getTypeArgument(getClass()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 消费幂等校验
        if (isRepeatMessage(msgId, message, raw)) {
            log.warn("[mq] message already consume");
            return true;
        }
        try {
            handleMessage(msgId, message, raw);
            log.info("[mq] message handle success");
        } catch (Exception e) {
            log.error("[mq] message handle error", e);
            throw new MQException(e);
        }
        return true;
    }

    protected abstract void handleMessage(String msgId, T message, RAW raw);

    protected boolean isRepeatMessage(String msgId, T data, RAW raw) {
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
