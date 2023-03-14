package com.ark.component.mq.integration.rocket;


import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MQMessageProcessor;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@MQMessageListener(
        consumerGroup = "GID_TEST",
        topic = MQTestConst.TOPIC2,
        tags = MQTestConst.TAG_CLUSTER,
        mq = MQType.RABBIT,
        consumeMode = ConsumeMode.CLUSTERING)
@Component
@Slf4j
public class ClusterTestProcessorMQ<T> implements MQMessageProcessor<Object> {

    @Override
    public void process(byte[] body, String msgId, Object s) throws MQException {
        log.info("cluster msgId = {}, body = {}", msgId, new String(body));
    }

}
