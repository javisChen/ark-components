package com.ark.component.mq.integration.rabbit;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.ark.component.mq.MQService;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.integration.ApplicationTests;
import com.ark.component.mq.integration.MQTestConst;
import com.ark.component.mq.rabbit.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class RabbitOrderCreatedTestProducer extends ApplicationTests {

    @Autowired
    private RabbitMQService mqService;

    @Test
    public void test() {
        JSONObject body = new JSONObject();
        body.put("orderId", IdUtil.fastSimpleUUID());
        MsgBody msgBody = new MsgBody(IdUtil.fastSimpleUUID(), body);
        mqService.send(MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
