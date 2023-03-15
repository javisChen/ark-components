package com.ark.component.mq.core.listener;

import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.MQType;

/**
 * MQ消费者监听行为
 * @param <RAW>
 */
public interface MQListener<RAW> {

    void listen(MessageHandler<RAW> processor, MQListenerConfig listenerConfig);

    MQType getMqType();

}
