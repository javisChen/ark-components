package com.ark.component.mq.integration;

import com.ark.component.mq.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageTemplate implements ApplicationContextAware {

    private Map<MQType, MQService> mqServiceHolder;

    public MQSendResponse send(String topic, MsgBody msg) {
        return send(MQType.ROCKET, topic, msg);
    }

    public MQSendResponse send(String topic, MsgBody msg, int timeout) {
        return send(MQType.ROCKET, topic, msg, timeout);
    }

    public MQSendResponse send(String topic, String tag, MsgBody msg) {
        return send(MQType.ROCKET, topic, tag, msg);
    }

    public MQSendResponse send(String topic, String tag, MsgBody msg, int timeout) {
        return send(MQType.ROCKET, topic, tag, msg, timeout);
    }

    /*
        同步发送延迟消息
     */
    public MQSendResponse delaySend(String topic, MsgBody msg, int delay) {
        return delaySend(MQType.ROCKET, topic, msg, delay);
    }

    public MQSendResponse delaySend(String topic, MsgBody msg, int delay, int timeout) {
        return delaySend(MQType.ROCKET, topic, msg, delay, timeout);
    }

    public MQSendResponse delaySend(String topic, String tag, int delay, MsgBody msg) {
        return delaySend(MQType.ROCKET, topic, tag, delay, msg);
    }

    public MQSendResponse delaySend(String topic, String tag, int delay, int timeout, MsgBody msg) {
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

    public void asyncSend(String topic, MsgBody msg, MQSendCallback callback) {
        asyncSend(MQType.ROCKET, topic, msg, callback);
    }

    public void asyncSend(String topic, MsgBody msg, int timeout, MQSendCallback callback) {
        asyncSend(MQType.ROCKET, topic, msg, timeout, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, MQSendCallback callback) {
        asyncSend(MQType.ROCKET, topic, tag, msg, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, int timeout, MQSendCallback callback) {
        asyncSend(MQType.ROCKET, topic, tag, msg, timeout, callback);
    }

    /*
        异步发送延迟消息
     */
    public void delayAsyncSend(String topic, MsgBody msg, int delay, MQSendCallback callback) {
        delayAsyncSend(MQType.ROCKET, topic, msg, delay, callback);
    }

    public void delayAsyncSend(String topic, MsgBody msg, int delay, int timeout, MQSendCallback callback) {
        delayAsyncSend(MQType.ROCKET, topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, MsgBody msg, MQSendCallback callback) {
        delayAsyncSend(MQType.ROCKET, topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody msg, MQSendCallback callback) {
        delayAsyncSend(MQType.ROCKET, topic, tag, delay, timeout, msg, callback);
    }

    public MQSendResponse send(MQType mqType, String topic, MsgBody msg) {
        return mqServiceHolder.get(mqType).send(topic, msg);
    }

    public MQSendResponse send(MQType mqType, String topic, MsgBody msg, int timeout) {
        return mqServiceHolder.get(mqType).send(topic, msg, timeout);
    }

    public MQSendResponse send(MQType mqType, String topic, String tag, MsgBody msg) {
        return mqServiceHolder.get(mqType).send(topic, tag, msg);
    }

    public MQSendResponse send(MQType mqType, String topic, String tag, MsgBody msg, int timeout) {
        return mqServiceHolder.get(mqType).send(topic, tag, msg, timeout);
    }

    /*
        同步发送延迟消息
     */
    public MQSendResponse delaySend(MQType mqType, String topic, MsgBody msg, int delay) {
        return mqServiceHolder.get(mqType).delaySend(topic, msg, delay);
    }

    public MQSendResponse delaySend(MQType mqType, String topic, MsgBody msg, int delay, int timeout) {
        return mqServiceHolder.get(mqType).delaySend(topic, msg, delay, timeout);
    }

    public MQSendResponse delaySend(MQType mqType, String topic, String tag, int delay, MsgBody msg) {
        return mqServiceHolder.get(mqType).delaySend(topic, tag, delay, msg);
    }

    public MQSendResponse delaySend(MQType mqType, String topic, String tag, int delay, int timeout, MsgBody msg) {
        return mqServiceHolder.get(mqType).delaySend(topic, tag, delay, timeout, msg);
    }

    /*
        异步发送
     */
    public void asyncSend(MQType mqType, String topic, MsgBody msg) {
        mqServiceHolder.get(mqType).asyncSend(topic, msg);
    }

    public void asyncSend(MQType mqType, String topic, MsgBody msg, int timeout) {
        mqServiceHolder.get(mqType).asyncSend(topic, msg, timeout);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg) {
        mqServiceHolder.get(mqType).asyncSend(topic, tag, msg);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg, int timeout) {
        mqServiceHolder.get(mqType).asyncSend(topic, tag, msg, timeout);
    }

    public void asyncSend(MQType mqType, String topic, MsgBody msg, MQSendCallback callback) {
        mqServiceHolder.get(mqType).asyncSend(topic, msg, callback);
    }

    public void asyncSend(MQType mqType, String topic, MsgBody msg, int timeout, MQSendCallback callback) {
        mqServiceHolder.get(mqType).asyncSend(topic, msg, timeout, callback);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg, MQSendCallback callback) {
        mqServiceHolder.get(mqType).asyncSend(topic, tag, msg, callback);
    }

    public void asyncSend(MQType mqType, String topic, String tag, MsgBody msg, int timeout, MQSendCallback callback) {
        mqServiceHolder.get(mqType).asyncSend(topic, tag, msg, timeout, callback);
    }

    /*
        异步发送延迟消息
     */
    public void delayAsyncSend(MQType mqType, String topic, MsgBody msg, int delay, MQSendCallback callback) {
        mqServiceHolder.get(mqType).delayAsyncSend(topic, msg, delay, callback);
    }

    public void delayAsyncSend(MQType mqType, String topic, MsgBody msg, int delay, int timeout, MQSendCallback callback) {
        mqServiceHolder.get(mqType).delayAsyncSend(topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(MQType mqType, String topic, String tag, int delay, MsgBody msg, MQSendCallback callback) {
        mqServiceHolder.get(mqType).delayAsyncSend(topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(MQType mqType, String topic, String tag, int delay, int timeout, MsgBody msg, MQSendCallback callback) {
        mqServiceHolder.get(mqType).delayAsyncSend(topic, tag, delay, timeout, msg, callback);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, MQService> mqServices = applicationContext.getBeansOfType(MQService.class);
        if (CollectionUtils.isEmpty(mqServices)) {
            return;
        }
        mqServiceHolder = new HashMap<>(mqServices.size());
        for (Map.Entry<String, MQService> serviceEntry : mqServices.entrySet()) {
            MQService mqService = serviceEntry.getValue();
            MQType mqType = mqService.mqType();
            mqServiceHolder.put(mqType, mqService);
        }
    }
}
