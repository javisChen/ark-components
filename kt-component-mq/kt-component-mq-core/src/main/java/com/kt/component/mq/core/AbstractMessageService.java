package com.kt.component.mq.core;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.MessageService;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMessageService<P, R> implements MessageService {
    private final MQConfiguration mqConfiguration;

    protected AbstractMessageService(MQConfiguration mqConfiguration) {
        this.mqConfiguration = mqConfiguration;
    }


    @Override
    public MessageResponse send(String topic, MessagePayLoad payLoad, int timeout) {
        return doSend(topic, null, payLoad, timeout, 0);
    }

    @Override
    public MessageResponse send(String topic, String tag, MessagePayLoad payLoad, int timeout) {
        return doSend(topic, null, payLoad, timeout, 0);
    }

    @Override
    public MessageResponse delaySend(String topic, MessagePayLoad payLoad, int delay) {
        return doSend(topic, null, payLoad, 0, delay);
    }

    @Override
    public MessageResponse delaySend(String topic, MessagePayLoad payLoad, int delay, int timeout) {
        return doSend(topic, null, payLoad, timeout, delay);
    }

    @Override
    public MessageResponse delaySend(String topic, String tag, int delay, MessagePayLoad payLoad) {
        return doSend(topic, tag, payLoad, 0, delay);
    }

    @Override
    public MessageResponse delaySend(String topic, String tag, int delay, int timeout, MessagePayLoad payLoad) {
        return doSend(topic, tag, payLoad, timeout, delay);
    }

    @Override
    public MessageResponse send(String topic, MessagePayLoad payLoad) {
        return doSend(topic, null, payLoad, mqConfiguration.getTimeout(), 0);
    }

    @Override
    public MessageResponse send(String topic, String tag, MessagePayLoad payLoad) {
        return doSend(topic, tag, payLoad, mqConfiguration.getTimeout(), 0);
    }

    @Override
    public void asyncSend(String topic, MessagePayLoad payLoad) {
        doAsyncSend(topic, null, payLoad, null, 0, 0);
    }

    @Override
    public void asyncSend(String topic, MessagePayLoad payLoad, int timeout) {
        doAsyncSend(topic, null, payLoad, null, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MessagePayLoad payLoad) {
        doAsyncSend(topic, null, payLoad, null, 0, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MessagePayLoad payLoad, int timeout) {
        doAsyncSend(topic, tag, payLoad, null, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, MessagePayLoad payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, 0, 0);
    }

    @Override
    public void asyncSend(String topic, MessagePayLoad payLoad, int timeout, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MessagePayLoad payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, 0, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MessagePayLoad payLoad, int timeout, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, timeout, 0);
    }

    @Override
    public void delayAsyncSend(String topic, MessagePayLoad payLoad, int delay, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, 0, delay);
    }

    @Override
    public void delayAsyncSend(String topic, MessagePayLoad payLoad, int delay, int timeout, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, timeout, delay);
    }

    @Override
    public void delayAsyncSend(String topic, String tag, int delay, MessagePayLoad payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, 0, delay);
    }

    @Override
    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MessagePayLoad payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, timeout, delay);
    }

    protected MessageResponse doSend(String topic, String tag, MessagePayLoad payLoad, long timeout, int delayLevel) {
        String destination = StrUtil.isNotBlank(tag) ? buildDestination(topic, tag) : topic;
        try {
            P body = buildBody(payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[mq] start send message destination:{} payLoad:{}",
                        destination, JSON.toJSONString(body));
            }
            R result = executeSend(destination, body, timeout, delayLevel);
            if (log.isDebugEnabled()) {
                log.debug("[mq] send message destination:{} payLoad:{}, msgId:{}",
                        destination, JSON.toJSONString(body), payLoad.getMsgId());
            }
            return convertToMQResponse(result);
        } catch (Exception e) {
            log.error("[mq] send message error", e);
            throw new MQException(e);
        }
    }

    protected abstract P buildBody(MessagePayLoad body);

    protected abstract MessageResponse convertToMQResponse(R result);

    protected abstract R executeSend(String destination, P messagePayLoad, long timeout, int delayLevel);

    protected void doAsyncSend(String topic, String tag, MessagePayLoad payLoad, MessageSendCallback callback, long timeout, int delayLevel) {
        String destination = StrUtil.isNotBlank(tag) ? buildDestination(topic, tag) : topic;
        try {
            P body = buildBody(payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[mq] start send message destination:{} payLoad:{}",
                        destination, JSON.toJSONString(body));
            }
            R result = executeSend(destination, body, timeout, delayLevel);
            if (log.isDebugEnabled()) {
                log.debug("[mq] send message destination:{} payLoad:{}, msgId:{}",
                        destination, JSON.toJSONString(body), payLoad.getMsgId());
            }
            return convertToMQResponse(result);
            Message<MessagePayLoad> message = MessageBuilder.withPayload(payLoad).build();
            if (log.isDebugEnabled()) {
                log.debug("[mq] start send message destination:{} payLoad:{}",
                        destination, JSON.toJSONString(payLoad));
            }
            rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    if (log.isDebugEnabled()) {
                        log.debug("[mq] send message success destination:{} payLoad:{}, msgId:{}",
                                destination, JSON.toJSONString(payLoad), sendResult.getMsgId());
                    }
                    if (callback != null) {
                        callback.onSuccess(convertToMQResponse(sendResult));
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[mq] send message error callback", throwable);
                    if (callback != null) {
                        callback.onException(throwable);
                    }
                }
            }, timeout, delayLevel);
        } catch (Exception e) {
            log.error("[mq] send message error", e);
            throw new MQException(e);
        }
    }

    protected abstract String buildDestination(String topic, String tag);
}
