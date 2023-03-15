package com.ark.component.mq.integration.rabbit;


import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.MQType;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.integration.MQTestConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(
        consumerGroup = "GID_TEST2",
        topic = MQTestConst.TOPIC_ORDER,
        tags = MQTestConst.TAG_ORDER_CREATED,
        mq = MQType.RABBIT,
        consumeMode = ConsumeMode.BROADCASTING)
@Component
@Slf4j
public class RabbitOrderCreatedMessageHandler<T> implements MessageHandler<Object> {

    @Override
    public void handle(byte[] body, String msgId, Object s) throws MQException {
        log.info("receive orderCreated: msgId = {}, body = {}", msgId, new String(body));
    }

}
