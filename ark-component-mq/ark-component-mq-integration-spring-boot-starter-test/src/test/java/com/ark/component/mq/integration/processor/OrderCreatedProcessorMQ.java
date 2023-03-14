package com.ark.component.mq.integration.processor;


import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.ConsumeExceptions;
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
public class OrderCreatedProcessorMQ<T> implements MQMessageProcessor<Object> {

    @Override
    public void process(byte[] body, String msgId, Object s) throws MQException {
        log.info("receive orderCreated: msgId = {}, body = {}", msgId, new String(body));
        throw ConsumeExceptions.discard("不消费...");
    }

}
