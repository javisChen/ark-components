package com.ark.component.mq.core.producer;

import com.ark.component.mq.Message;
import com.ark.component.mq.MessageResponse;
import com.ark.component.mq.MessageSendCallback;
import com.ark.component.mq.MQMessageService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProducer {

    private final MQMessageService MQMessageService;

    public MessageProducer(MQMessageService MQMessageService) {
        this.MQMessageService = MQMessageService;
    }

    public MessageResponse send(String topic, Message msg) {
        return MQMessageService.send(topic, msg);
    }

    public MessageResponse send(String topic, Message msg, int timeout) {
        return MQMessageService.send(topic, msg, timeout);
    }

    public MessageResponse send(String topic, String tag, Message msg) {
        return MQMessageService.send(topic, tag, msg);
    }

    public MessageResponse send(String topic, String tag, Message msg, int timeout) {
        return MQMessageService.send(topic, tag, msg, timeout);
    }

    public MessageResponse delaySend(String topic, Message msg, int delay) {
        return MQMessageService.delaySend(topic, msg, delay);
    }

    public MessageResponse delaySend(String topic, Message msg, int delay, int timeout) {
        return MQMessageService.delaySend(topic, msg, delay, timeout);
    }

    public MessageResponse delaySend(String topic, String tag, int delay, Message msg) {
        return MQMessageService.delaySend(topic, tag, delay, msg);
    }

    public MessageResponse delaySend(String topic, String tag, int delay, int timeout, Message msg) {
        return MQMessageService.delaySend(topic, tag, delay, timeout, msg);
    }

    public void asyncSend(String topic, Message msg) {
        MQMessageService.asyncSend(topic, msg);
    }

    public void asyncSend(String topic, Message msg, int timeout) {
        MQMessageService.asyncSend(topic, msg, timeout);
    }

    public void asyncSend(String topic, String tag, Message msg) {
        MQMessageService.asyncSend(topic, tag, msg);
    }

    public void asyncSend(String topic, String tag, Message msg, int timeout) {
        MQMessageService.asyncSend(topic, tag, msg, timeout);
    }

    public void asyncSend(String topic, Message msg, MessageSendCallback callback) {
        MQMessageService.asyncSend(topic, msg, callback);
    }

    public void asyncSend(String topic, Message msg, int timeout, MessageSendCallback callback) {
        MQMessageService.asyncSend(topic, msg, timeout, callback);
    }

    public void asyncSend(String topic, String tag, Message msg, MessageSendCallback callback) {
        MQMessageService.asyncSend(topic, tag, msg, callback);
    }

    public void asyncSend(String topic, String tag, Message msg, int timeout, MessageSendCallback callback) {
        MQMessageService.asyncSend(topic, tag, msg, timeout, callback);
    }

    public void delayAsyncSend(String topic, Message msg, int delay, MessageSendCallback callback) {
        MQMessageService.asyncSend(topic, msg, delay, callback);
    }

    public void delayAsyncSend(String topic, Message msg, int delay, int timeout, MessageSendCallback callback) {
        MQMessageService.delayAsyncSend(topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, Message msg, MessageSendCallback callback) {
        MQMessageService.delayAsyncSend(topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, int timeout, Message msg, MessageSendCallback callback) {
        MQMessageService.delayAsyncSend(topic, tag, delay, timeout, msg, callback);
    }

}
