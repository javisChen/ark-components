package com.ark.component.mq.integration;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.ark.component.mq.MQType;
import com.ark.component.mq.MsgBody;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageTemplateTest extends ApplicationTests {

    @Autowired
    private MessageTemplate messageTemplate;

    @Test
    public void testSendToRabbit() {
        MsgBody msgBody = buildBody();
        messageTemplate.send(MQType.RABBIT, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSendToRocket() {
        MsgBody msgBody = buildBody();
        messageTemplate.send(MQType.ROCKET, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
    }

    @Test
    public void testSendToKafka() {
        MsgBody msgBody = buildBody();
        messageTemplate.send(MQType.KAFKA, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
    }

    @Test
    public void testSendToPulsar() {
        MsgBody msgBody = buildBody();
        messageTemplate.send(MQType.PULSAR, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
    }

    private MsgBody buildBody() {
        JSONObject body = new JSONObject();
        body.put("orderId", IdUtil.fastSimpleUUID());
        return new MsgBody(IdUtil.fastSimpleUUID(), body);
    }

}
