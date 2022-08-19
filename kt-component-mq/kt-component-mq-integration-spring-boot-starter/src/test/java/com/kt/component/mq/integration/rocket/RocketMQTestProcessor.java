package com.kt.component.mq.integration.rocket;

import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.core.annotations.MQMessageListener;
import com.kt.component.mq.core.processor.StandardMessageProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(consumerGroup = "cg_test", topic = "test")
@Component
@Slf4j
public class RocketMQTestProcessor extends StandardMessageProcessor<MessagePayLoad> {

    @Override
    protected void handleMessage(MessagePayLoad message) {
        log.info("RocketMQTestProcessor Consume Msg -> {}", message);
    }
}
