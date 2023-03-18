package com.ark.component.mq.integration.rocket;


import com.ark.component.mq.MQType;
import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.integration.MQTestConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(
        consumerGroup = "GID_ORDER_GROUP",
        topic = MQTestConst.TOPIC_ORDER,
        tags = MQTestConst.TAG_ORDER_CREATED,
        mq = MQType.ROCKET
)
@Component
@Slf4j
public class RocketOrderCreatedMessageHandler<T> implements MessageHandler<Object> {

    @Override
    public void handle(byte[] body, String msgId, Object s) throws MQException {
        log.info("rocket receive orderCreated: msgId = {}, body = {}", msgId, new String(body));
    }

}
