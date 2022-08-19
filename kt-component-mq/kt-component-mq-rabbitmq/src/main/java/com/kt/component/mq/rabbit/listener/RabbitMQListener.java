package com.kt.component.mq.rabbit.listener;

import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.listener.MQListener;
import com.kt.component.mq.core.listener.MQListenerConfig;
import com.kt.component.mq.core.processor.MQMessageProcessor;

public class RabbitMQListener implements MQListener {

    @Override
    public void listen(MQMessageProcessor processor, MQConfiguration mqConfiguration, MQListenerConfig annotation) {

    }
}
