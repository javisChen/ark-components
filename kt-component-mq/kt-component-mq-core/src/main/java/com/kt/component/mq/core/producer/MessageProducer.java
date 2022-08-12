package com.kt.component.mq.core.producer;

import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.MessageService;
import com.kt.component.mq.core.generator.MessageIdGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProducer {

    private MessageIdGenerator messageIdGenerator;

    private final MessageService messageService;

    public MessageProducer(MessageService messageService) {
        this.messageService = messageService;
    }

    public MessageResponse send(String topic, MessagePayLoad msg) {
        return messageService.send(topic, msg);
    }

    public MessageResponse send(String topic, MessagePayLoad msg, int timeout) {
        return messageService.send(topic, msg, timeout);
    }

    public MessageResponse send(String topic, String tag, MessagePayLoad msg) {
        return messageService.send(topic, tag, msg);
    }

    public MessageResponse send(String topic, String tag, MessagePayLoad msg, int timeout) {
        return messageService.send(topic, tag, msg, timeout);
    }

    public MessageResponse delaySend(String topic, MessagePayLoad msg, int delay) {
        return messageService.delaySend(topic, msg, delay);
    }

    public MessageResponse delaySend(String topic, MessagePayLoad msg, int delay, int timeout) {
        return messageService.delaySend(topic, msg, delay, timeout);
    }

    public MessageResponse delaySend(String topic, String tag, int delay, MessagePayLoad msg) {
        return messageService.delaySend(topic, tag, delay, msg);
    }

    public MessageResponse delaySend(String topic, String tag, int delay, int timeout, MessagePayLoad msg) {
        return messageService.delaySend(topic, tag, delay, timeout, msg);
    }

    public void asyncSend(String topic, MessagePayLoad msg) {
        messageService.asyncSend(topic, msg);
    }

    public void asyncSend(String topic, MessagePayLoad msg, int timeout) {
        messageService.asyncSend(topic, msg, timeout);
    }

    public void asyncSend(String topic, String tag, MessagePayLoad msg) {
        messageService.asyncSend(topic, tag, msg);
    }

    public void asyncSend(String topic, String tag, MessagePayLoad msg, int timeout) {
        messageService.asyncSend(topic, tag, msg, timeout);
    }

    public void asyncSend(String topic, MessagePayLoad msg, MessageSendCallback callback) {
        messageService.asyncSend(topic, msg, callback);
    }

    public void asyncSend(String topic, MessagePayLoad msg, int timeout, MessageSendCallback callback) {
        messageService.asyncSend(topic, msg, timeout, callback);
    }

    public void asyncSend(String topic, String tag, MessagePayLoad msg, MessageSendCallback callback) {
        messageService.asyncSend(topic, tag, msg, callback);
    }

    public void asyncSend(String topic, String tag, MessagePayLoad msg, int timeout, MessageSendCallback callback) {
        messageService.asyncSend(topic, tag, msg, timeout, callback);
    }

    public void delayAsyncSend(String topic, MessagePayLoad msg, int delay, MessageSendCallback callback) {
        messageService.asyncSend(topic, msg, delay, callback);
    }

    public void delayAsyncSend(String topic, MessagePayLoad msg, int delay, int timeout, MessageSendCallback callback) {
        messageService.delayAsyncSend(topic, msg, delay, timeout, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, MessagePayLoad msg, MessageSendCallback callback) {
        messageService.delayAsyncSend(topic, tag, delay, msg, callback);
    }

    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MessagePayLoad msg, MessageSendCallback callback) {
        messageService.delayAsyncSend(topic, tag, delay, timeout, msg, callback);
    }

}
