package com.ark.component.mq.integration.rocket;


import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(
        consumerGroup = "GID_TEST2",
        topic = MQTestConst.TOPIC,
        tags = MQTestConst.TAG_BROAD_CASTING,
        consumeMode = ConsumeMode.BROADCASTING)
@Component
@Slf4j
public class BroadCastingTestProcessorMQ<T> implements MQMessageProcessor<Object> {

    @Override
    public void process(byte[] body, String msgId, Object s) throws MQException {
        log.info("broadCasting msgId = {}, body = {}", msgId, new String(body));
    }

}
