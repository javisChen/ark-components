package com.ark.component.mq.core.producer;

import com.ark.component.mq.MsgBody;
import com.ark.component.mq.MQSendResponse;
import com.ark.component.mq.MQSendCallback;
import com.ark.component.mq.MQService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProducer {

    private final MQService MQService;

    public MessageProducer(MQService MQService) {
        this.MQService = MQService;
    }

    public MQSendResponse send(String topic, MsgBody msg) {
        return MQService.send(topic, msg);
    }

    public MQSendResponse send(String topic, MsgBody msg, int timeout) {
        return MQService.send(topic, msg, timeout);
    }

    public MQSendResponse send(String topic, String tag, MsgBody msg) {
        return MQService.send(topic, tag, msg);
    }

    public MQSendResponse send(String topic, String tag, MsgBody msg, int timeout) {
        return MQService.send(topic, tag, msg, timeout);
    }

    public MQSendResponse delaySend(String topic, MsgBody msg, int delay) {
        return MQService.delaySend(topic, msg, delay);
    }

    public MQSendResponse delaySend(String topic, MsgBody msg, int delay, int timeout) {
        return MQService.delaySend(topic, msg, delay, timeout);
    }

    public MQSendResponse delaySend(String topic, String tag, int delay, MsgBody msg) {
        return MQService.delaySend(topic, tag, delay, msg);
    }

    public MQSendResponse delaySend(String topic, String tag, int delay, int timeout, MsgBody msg) {
        return MQService.delaySend(topic, tag, delay, timeout, msg);
    }

    public void asyncSend(String topic, MsgBody msg) {
        MQService.asyncSend(topic, msg);
    }

    public void asyncSend(String topic, MsgBody msg, int timeout) {
        MQService.asyncSend(topic, msg, timeout);
    }

    public void asyncSend(String topic, String tag, MsgBody msg) {
        MQService.asyncSend(topic, tag, msg);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, int timeout) {
        MQService.asyncSend(topic, tag, msg, timeout);
    }

    public void asyncSend(String topic, MsgBody msg, MQSendCallback callback) {
        MQService.asyncSend(topic, msg, callback);
    }

    public void asyncSend(String topic, MsgBody msg, int timeout, MQSendCallback callback) {
        MQService.asyncSend(topic, msg, timeout, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, MQSendCallback callback) {
        MQService.asyncSend(topic, tag, msg, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, int timeout, MQSendCallback callback) {
        MQService.asyncSend(topic, tag, msg, timeout, callback);
    }

    public void delayAsyncSend(String topic, MsgBody msg, int delay, MQSendCallback callback) {
        MQService.asyncSend(topic, msg, delay, callback);
    }

    public void delayAsyncSend(String topic, MsgBody msg, int delay, int timeout, MQSendCallback callback) {
        MQService.delayAsyncSend(topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, MsgBody msg, MQSendCallback callback) {
        MQService.delayAsyncSend(topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody msg, MQSendCallback callback) {
        MQService.delayAsyncSend(topic, tag, delay, timeout, msg, callback);
    }

}
