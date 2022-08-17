package com.kt.component.mq.core.consumer;

import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MQMessageProcessor<T> {

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
