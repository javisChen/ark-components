package com.kt.component.mq.integration.rocket;


import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.core.annotations.MQMessageListener;
import com.kt.component.mq.core.processor.SimpleMQMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(consumerGroup = "cg_test", topic = "test")
@Component
@Slf4j
public class RocketMQTestProcessorMQ extends SimpleMQMessageProcessor<MessagePayLoad> {

    @Override
    protected void handleMessage(String msgId, MessagePayLoad message, Object o) {

    }

}
