package com.ark.component.mq.integration.processor;


import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.MQExceptions;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.integration.MQTestConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(
        consumerGroup = "GID_TEST",
        topic = MQTestConst.TOPIC2,
        tags = MQTestConst.TAG_ORDER_CREATED,
        mq = MQType.RABBIT,
        consumeMode = ConsumeMode.CLUSTERING)
@Component
@Slf4j
public class ClusterTestProcessor<T> implements MessageHandler<Object> {

    @Override
    public void handle(byte[] body, String msgId, Object raw) throws MQException {
        log.info("cluster msgId = {}, body = {}", msgId, new String(body));
        throw MQExceptions.requeue("暂不消费...");
    }

}
