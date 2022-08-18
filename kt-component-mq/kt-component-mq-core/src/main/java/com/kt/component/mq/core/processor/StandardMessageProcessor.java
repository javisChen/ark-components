package com.kt.component.mq.core.processor;

import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;

/**
 * 一个处理消息的框架。极大多数情况，只需要继承当前Processor
 * 1.消息反序列化
 * 2.消息幂等校验
 * 3.消息处理
 * @param <T>
 */
@Slf4j
public abstract class StandardMessageProcessor<T> implements MQMessageProcessor {

    @Override
    public boolean process(String msgId, byte[] body) {
        // 反序列化
        T message = deserializeBody(body);
        // 消费幂等校验
        boolean isRepeat = isRepeatMessage(message);
        if (isRepeat) {
            log.warn("[mq] message already consume");
            return true;
        }
        try {
            handleMessage(message);
            log.error("[mq] message handle success");
        } catch (Exception e) {
            log.error("[mq] message handle error", e);
            throw new MQException(e);
        }
        return true;
    }

    protected T deserializeBody(byte[] body) {
        return JSON.parseObject(body, MessagePayLoad.class);
    }

    protected abstract void handleMessage(T message);

    protected boolean isRepeatMessage(T data) {
        return false;
    }
}
