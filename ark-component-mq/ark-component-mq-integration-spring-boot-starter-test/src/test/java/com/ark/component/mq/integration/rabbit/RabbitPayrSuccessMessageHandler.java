package com.ark.component.mq.integration.rabbit;


import com.ark.component.mq.MQType;
import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.integration.MQTestConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(
        topic = MQTestConst.TOPIC_PAY,
        tags = MQTestConst.TAG_PAY_NOTIFY,
        mq = MQType.RABBIT,
        consumeMode = ConsumeMode.CLUSTERING)
@Component
@Slf4j
public class RabbitPayrSuccessMessageHandler<T> implements MessageHandler<Object> {

    @Override
    public void handle(byte[] body, String msgId, Object s) throws MQException {
        log.info("rabbit receive payNotify: msgId = {}, body = {}", msgId, new String(body));
    }

}
