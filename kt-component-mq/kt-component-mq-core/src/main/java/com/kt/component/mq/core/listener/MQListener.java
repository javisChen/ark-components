package com.kt.component.mq.core.listener;

import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.support.MQType;

public interface MQListener {

    void listen(MQMessageProcessor processor, MQListenerConfig annotation);

    MQType mqType();

}
