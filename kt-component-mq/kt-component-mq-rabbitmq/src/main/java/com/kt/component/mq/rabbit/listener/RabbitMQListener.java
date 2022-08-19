package com.kt.component.mq.rabbit.listener;

import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.processor.MQMessageProcessor;
import com.kt.component.mq.core.support.MQType;

public class RabbitMQListener implements MQListener {

    @Override
    public void listen(MQMessageProcessor processor,
                       MQListenerConfig annotation) {

    }

    @Override
    public MQType mqType() {
        return MQType.RABBIT;
    }
}
