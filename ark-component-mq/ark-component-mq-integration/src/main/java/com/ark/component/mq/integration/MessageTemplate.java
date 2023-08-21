package com.ark.component.mq.integration;

import cn.hutool.core.lang.Assert;
import com.ark.component.mq.*;
import com.ark.component.mq.exception.MQException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageTemplate implements ApplicationContextAware {

    private Map<MQType, MQService> mqServiceHolder;

    public SendResult send(String topic, MsgBody msg) {
        return send(MQType.ROCKET, topic, msg);
    }

    public SendResult send(String topic, MsgBody msg, int timeout) {
        return send(MQType.ROCKET, topic, msg, timeout);
    }

    public SendResult send(String topic, String tag, MsgBody msg) {
        return send(MQType.ROCKET, topic, tag, msg);
    }

    public SendResult send(String topic, String tag, MsgBody msg, int timeout) {
        return send(MQType.ROCKET, topic, tag, msg, timeout);
    }

    /*
        同步发送延迟消息
     */
    public SendResult delaySend(String topic, MsgBody msg, int delay) {
        return delaySend(MQType.ROCKET, topic, msg, delay);
    }

    public SendResult delaySend(String topic, MsgBody msg, int delay, int timeout) {
        return delaySend(MQType.ROCKET, topic, msg, delay, timeout);
    }

    public SendResult delaySend(String topic, String tag, int delay, MsgBody msg) {
        return delaySend(MQType.ROCKET, topic, tag, delay, msg);
    }

    public SendResult delaySend(String topic, String tag, int delay, int timeout, MsgBody msg) {
        return delaySend(MQType.ROCKET, topic, tag, delay, timeout, msg);
    }

    /*
        异步发送
     */
    public void asyncSend(String topic, MsgBody msg) {
        asyncSend(MQType.ROCKET, topic, msg);
    }

    public void asyncSend(String topic, MsgBody msg, int timeout) {
        asyncSend(MQType.ROCKET, topic, msg, timeout);
    }

    public void asyncSend(String topic, String tag, MsgBody msg) {
        asyncSend(MQType.ROCKET, topic, tag, msg);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, int timeout) {
        asyncSend(MQType.ROCKET, topic, tag, msg, timeout);
    }

    public void asyncSend(String topic, MsgBody msg, SendConfirm callback) {
        asyncSend(MQType.ROCKET, topic, msg, callback);
    }

    public void asyncSend(String topic, MsgBody msg, int timeout, SendConfirm callback) {
        asyncSend(MQType.ROCKET, topic, msg, timeout, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, SendConfirm callback) {
        asyncSend(MQType.ROCKET, topic, tag, msg, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, int timeout, SendConfirm callback) {
        asyncSend(MQType.ROCKET, topic, tag, msg, timeout, callback);
    }

    /*
        异步发送延迟消息
     */
    public void delayAsyncSend(String topic, MsgBody msg, int delay, SendConfirm callback) {
        delayAsyncSend(MQType.ROCKET, topic, msg, delay, callback);
    }

    public void delayAsyncSend(String topic, MsgBody msg, int delay, int timeout, SendConfirm callback) {
        delayAsyncSend(MQType.ROCKET, topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, MsgBody msg, SendConfirm callback) {
        delayAsyncSend(MQType.ROCKET, topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody msg, SendConfirm callback) {
        delayAsyncSend(MQType.ROCKET, topic, tag, delay, timeout, msg, callback);
    }

    public SendResult send(MQType mqType, String topic, MsgBody msg) {
        return getMqService(mqType).send(topic, msg);
    }

    public SendResult send(MQType mqType, String topic, MsgBody msg, int timeout) {
        return getMqService(mqType).send(topic, msg, timeout);
    }

    public SendResult send(MQType mqType, String topic, String tag, MsgBody msg) {
        return getMqService(mqType).send(topic, tag, msg);
    }

    public SendResult send(MQType mqType, String topic, String tag, MsgBody msg, int timeout) {
        return getMqService(mqType).send(topic, tag, msg, timeout);
    }

    /*
        同步发送延迟消息
     */
    public SendResult delaySend(MQType mqType, String topic, MsgBody msg, int delay) {
        return getMqService(mqType).delaySend(topic, msg, delay);
    }

    public SendResult delaySend(MQType mqType, String topic, MsgBody msg, int delay, int timeout) {
        return getMqService(mqType).delaySend(topic, msg, delay, timeout);
    }

    public SendResult delaySend(MQType mqType, String topic, String tag, int delay, MsgBody msg) {
        return getMqService(mqType).delaySend(topic, tag, delay, msg);
    }

    public SendResult delaySend(MQType mqType, String topic, String tag, int delay, int timeout, MsgBody msg) {
        return getMqService(mqType).delaySend(topic, tag, delay, timeout, msg);
    }

    /*
        异步发送
     */
    public void asyncSend(MQType mqType, String topic, MsgBody msg) {
        getMqService(mqType).asyncSend(topic, msg);
    }

    public void asyncSend(MQType mqType, String topic, MsgBody msg, int timeout) {
        getMqService(mqType).asyncSend(topic, msg, timeout);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg) {
        getMqService(mqType).asyncSend(topic, tag, msg);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg, int timeout) {
        getMqService(mqType).asyncSend(topic, tag, msg, timeout);
    }

    public void asyncSend(MQType mqType, String topic, MsgBody msg, SendConfirm callback) {
        getMqService(mqType).asyncSend(topic, msg, callback);
    }

    public void asyncSend(MQType mqType, String topic, MsgBody msg, int timeout, SendConfirm callback) {
        getMqService(mqType).asyncSend(topic, msg, timeout, callback);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg, SendConfirm callback) {
        getMqService(mqType).asyncSend(topic, tag, msg, callback);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg, int timeout, SendConfirm callback) {
        getMqService(mqType).asyncSend(topic, tag, msg, timeout, callback);
    }

    /*
        异步发送延迟消息
     */
    public void delayAsyncSend(MQType mqType, String topic, MsgBody msg, int delay, SendConfirm callback) {
        getMqService(mqType).delayAsyncSend(topic, msg, delay, callback);
    }

    public void delayAsyncSend(MQType mqType, String topic, MsgBody msg, int delay, int timeout, SendConfirm callback) {
        getMqService(mqType).delayAsyncSend(topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(MQType mqType, String topic, String tag, int delay, MsgBody msg, SendConfirm callback) {
        getMqService(mqType).delayAsyncSend(topic, tag, delay, msg, callback);
    }

    private MQService getMqService(MQType mqType) {
        MQService mqService = mqServiceHolder.get(mqType);
        Assert.notNull(mqService, () -> new MQException("[MQ]:[" + mqType.getName() + "]MQ服务未注入"));
        return mqService;
    }

    public void delayAsyncSend(MQType mqType, String topic, String tag, int delay, int timeout, MsgBody msg, SendConfirm callback) {
        getMqService(mqType).delayAsyncSend(topic, tag, delay, timeout, msg, callback);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, MQService> mqServices = applicationContext.getBeansOfType(MQService.class);
        if (CollectionUtils.isEmpty(mqServices)) {
            throw new ApplicationContextException("使用 ark-component-mq-integration 必须引入至少一种MQ的实现依赖包");
        }
        mqServiceHolder = new HashMap<>(mqServices.size());
        for (Map.Entry<String, MQService> serviceEntry : mqServices.entrySet()) {
            MQService mqService = serviceEntry.getValue();
            MQType mqType = mqService.mqType();
            mqServiceHolder.put(mqType, mqService);
        }
    }
}
