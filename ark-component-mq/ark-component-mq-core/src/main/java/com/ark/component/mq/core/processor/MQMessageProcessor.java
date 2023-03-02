package com.ark.component.mq.core.processor;

import com.ark.component.mq.exception.MQException;

/**
 *
 * @param <RAW> 各个MQ的原生消息对象
 */
public interface MQMessageProcessor<RAW> {

    void process(byte[] body, String msgId, RAW raw) throws MQException;

}
