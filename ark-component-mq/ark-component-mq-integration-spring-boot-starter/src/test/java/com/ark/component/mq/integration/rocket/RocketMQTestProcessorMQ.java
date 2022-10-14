package com.ark.component.mq.integration.rocket;


import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.SimpleMQMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(consumerGroup = "cg_test", topic = "test")
@Component
@Slf4j
public class RocketMQTestProcessorMQ<T> extends SimpleMQMessageProcessor<T> {

    @Override
    protected void handleMessage(String msgId, String sendId, T body, Object o) {

    }
}