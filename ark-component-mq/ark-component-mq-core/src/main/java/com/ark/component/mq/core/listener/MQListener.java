package com.ark.component.mq.core.listener;

import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.MQType;

public interface MQListener<RAW> {

    void listen(MQMessageProcessor<RAW> processor, MQListenerConfig annotation);

    MQType getMqType();

}
