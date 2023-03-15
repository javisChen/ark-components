package com.ark.component.mq.integration.rabbit;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.ark.component.mq.MQService;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.core.annotations.MQMessageListener;
import com.ark.component.mq.core.processor.MessageHandler;
import com.ark.component.mq.core.support.ConsumeMode;
import com.ark.component.mq.core.support.MQType;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.integration.ApplicationTests;
import com.ark.component.mq.integration.MQTestConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreatedTestProducer<T> extends ApplicationTests {

    @Autowired
    private MQService mqService;


    @Test
    public void test() {
        JSONObject body = new JSONObject();
        body.put("orderId", IdUtil.fastSimpleUUID());
        MsgBody msgBody = new MsgBody(IdUtil.fastSimpleUUID(), body);
        mqService.send(MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
    }
}
