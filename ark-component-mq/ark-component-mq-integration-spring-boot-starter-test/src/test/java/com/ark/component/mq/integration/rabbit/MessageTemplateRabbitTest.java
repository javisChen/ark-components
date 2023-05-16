package com.ark.component.mq.integration.rabbit;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.ark.component.mq.SendConfirm;
import com.ark.component.mq.SendResult;
import com.ark.component.mq.MQType;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.integration.ApplicationTests;
import com.ark.component.mq.integration.MQTestConst;
import com.ark.component.mq.integration.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

@Slf4j
public class MessageTemplateRabbitTest extends ApplicationTests {

    @Autowired
    private MessageTemplate messageTemplate;

    @Test
    public void testSendFanout() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 5 ; i++) {
            MsgBody msgBody = buildBody(i + 1);
            new Thread(() -> {
                SendResult sendResponse = messageTemplate.send(MQType.RABBIT, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody);
                log.info("推送成功：{}", sendResponse.getBizKey());
            }).start();
            new Thread(() -> {
                SendResult sendResponse = messageTemplate.send(MQType.RABBIT, MQTestConst.TOPIC_PAY, MQTestConst.TAG_PAY_NOTIFY, msgBody);
                log.info("推送成功：{}", sendResponse.getBizKey());
            }).start();
        }
        stopWatch.stop();
        log.info("耗时：{}", stopWatch.getLastTaskTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSendFanoutAsync() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 10 ; i++) {
            MsgBody msgBody = buildBody(i + 1);
            new Thread(()-> {
                messageTemplate.asyncSend(MQType.RABBIT, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody, new SendConfirm() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("推送成功：response = " + sendResult);
                    }

                    @Override
                    public void onException(SendResult sendResult) {
                        log.info("推送失败：response = " + sendResult);
                    }
                });
            }).start();
        }
        stopWatch.stop();
        log.info("耗时：{}", stopWatch.getLastTaskTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSendTopic() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 1; i++) {
            MsgBody msgBody = buildBody(i + 1);
            SendResult sendResponse = messageTemplate.send(MQType.RABBIT, MQTestConst.TOPIC_PAY, MQTestConst.TAG_PAY_NOTIFY, msgBody);
            log.info("消费成功：{}", sendResponse.getBizKey());
        }
        stopWatch.stop();
        log.info("耗时：{}", stopWatch.getLastTaskTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAsyncSend() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 1000; i++) {
            MsgBody msgBody = buildBody(i + 1);
            messageTemplate.asyncSend(MQType.RABBIT, MQTestConst.TOPIC_ORDER, MQTestConst.TAG_ORDER_CREATED, msgBody, getCallback());
        }
        stopWatch.stop();
        log.info("耗时：{}", stopWatch.getLastTaskTimeMillis());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private SendConfirm getCallback() {
        return new SendConfirm() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("消费成功：{}", sendResult.getBizKey());
            }

            @Override
            public void onException(SendResult sendResult) {

            }
        };
    }

    private MsgBody buildBody() {
        JSONObject body = new JSONObject();
        body.put("orderId", IdUtil.fastSimpleUUID());
        return new MsgBody(IdUtil.fastSimpleUUID(), body);
    }
    private MsgBody buildBody(int no) {
        JSONObject body = new JSONObject();
        body.put("orderId", IdUtil.fastSimpleUUID());
        String bizKey = String.valueOf(no);
        return new MsgBody(bizKey, body);
    }

}
