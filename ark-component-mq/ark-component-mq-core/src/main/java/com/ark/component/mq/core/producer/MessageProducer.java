package com.ark.component.mq.core.producer;

import com.ark.component.mq.MsgBody;
import com.ark.component.mq.SendResult;
import com.ark.component.mq.SendConfirm;
import com.ark.component.mq.MQService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProducer {

    private final MQService MQService;

    public MessageProducer(MQService MQService) {
        this.MQService = MQService;
    }

    public SendResult send(String topic, MsgBody msg) {
        return MQService.send(topic, msg);
    }

    public SendResult send(String topic, MsgBody msg, int timeout) {
        return MQService.send(topic, msg, timeout);
    }

    public SendResult send(String topic, String tag, MsgBody msg) {
        return MQService.send(topic, tag, msg);
    }

    public SendResult send(String topic, String tag, MsgBody msg, int timeout) {
        return MQService.send(topic, tag, msg, timeout);
    }

    public SendResult delaySend(String topic, MsgBody msg, int delay) {
        return MQService.delaySend(topic, msg, delay);
    }

    public SendResult delaySend(String topic, MsgBody msg, int delay, int timeout) {
        return MQService.delaySend(topic, msg, delay, timeout);
    }

    public SendResult delaySend(String topic, String tag, int delay, MsgBody msg) {
        return MQService.delaySend(topic, tag, delay, msg);
    }

    public SendResult delaySend(String topic, String tag, int delay, int timeout, MsgBody msg) {
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

    public void asyncSend(String topic, MsgBody msg, SendConfirm callback) {
        MQService.asyncSend(topic, msg, callback);
    }

    public void asyncSend(String topic, MsgBody msg, int timeout, SendConfirm callback) {
        MQService.asyncSend(topic, msg, timeout, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, SendConfirm callback) {
        MQService.asyncSend(topic, tag, msg, callback);
    }

    public void asyncSend(String topic, String tag, MsgBody msg, int timeout, SendConfirm callback) {
        MQService.asyncSend(topic, tag, msg, timeout, callback);
    }

    public void delayAsyncSend(String topic, MsgBody msg, int delay, SendConfirm callback) {
        MQService.asyncSend(topic, msg, delay, callback);
    }

    public void delayAsyncSend(String topic, MsgBody msg, int delay, int timeout, SendConfirm callback) {
        MQService.delayAsyncSend(topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, MsgBody msg, SendConfirm callback) {
        MQService.delayAsyncSend(topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody msg, SendConfirm callback) {
        MQService.delayAsyncSend(topic, tag, delay, timeout, msg, callback);
    }

}
