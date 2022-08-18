package com.kt.component.mq.core.listener;

import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.processor.MQMessageProcessor;

public interface MQListener {

    void listen(MQMessageProcessor processor, MQConfiguration mqConfiguration, MQListenerConfig annotation);

}
