package com.ark.component.mq.core.processor;

import com.ark.component.mq.exception.MQException;

/**
 * 统一抽象消息处理接口
 *
 * @param <RAW> 各个MQ的原生消息对象
 */
public interface MessageHandler<RAW> {

    /**
     * 处理消息
     *
     * @param body  消息内容字节数组
     * @param msgId 消息id
     * @param raw   各个MQ的原生对象
     * @throws MQException 抛出MQ异常
     */
    void handle(byte[] body, String msgId, RAW raw) throws MQException;

}
