package com.ark.component.mq.core.processor;

/**
 *
 * @param <RAW> 各个MQ的原生消息对象
 */
public interface MQMessageProcessor<RAW> {

    boolean process(byte[] body, String msgId, RAW raw);

}
