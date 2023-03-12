package com.ark.component.mq.rabbit.listener;

import com.ark.component.mq.core.listener.MQListener;
import com.ark.component.mq.core.listener.MQListenerConfig;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.MQType;

public class RabbitMQListener implements MQListener {

    @Override
    public void listen(MQMessageProcessor processor,
                       MQListenerConfig annotation) {

    }

    @Override
    public MQType getMqType() {
        return MQType.RABBIT;
    }
}
