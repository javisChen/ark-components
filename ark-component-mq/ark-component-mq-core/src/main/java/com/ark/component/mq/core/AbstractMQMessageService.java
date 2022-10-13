package com.ark.component.mq.core;

import com.alibaba.fastjson.JSON;
import com.ark.component.mq.MQMessageService;
import com.ark.component.mq.Message;
import com.ark.component.mq.MessageResponse;
import com.ark.component.mq.MessageSendCallback;
import com.ark.component.mq.configuation.MQConfiguration;
import com.ark.component.mq.core.generator.MessageIdGenerator;
import com.ark.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @param <P> MQ发送对象
 * @param <R> MQ发送响应对象
 */
@Slf4j
public abstract class AbstractMQMessageService<P, R> implements MQMessageService, ApplicationContextAware {
    private final MQConfiguration mqConfiguration;

    private MessageIdGenerator messageIdGenerator;

    protected AbstractMQMessageService(MQConfiguration mqConfiguration) {
        this.mqConfiguration = mqConfiguration;
    }

    protected AbstractMQMessageService() {
        this.mqConfiguration = new MQConfiguration();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.messageIdGenerator = applicationContext.getBean(MessageIdGenerator.class);
    }

    @Override
    public MessageResponse send(String topic, Message payLoad, int timeout) {
        return doSend(topic, null, payLoad, timeout, 0);
    }

    @Override
    public MessageResponse send(String topic, String tag, Message payLoad, int timeout) {
        return doSend(topic, null, payLoad, timeout, 0);
    }

    @Override
    public MessageResponse delaySend(String topic, Message payLoad, int delay) {
        return doSend(topic, null, payLoad, 0, delay);
    }

    @Override
    public MessageResponse delaySend(String topic, Message payLoad, int delay, int timeout) {
        return doSend(topic, null, payLoad, timeout, delay);
    }

    @Override
    public MessageResponse delaySend(String topic, String tag, int delay, Message payLoad) {
        return doSend(topic, tag, payLoad, 0, delay);
    }

    @Override
    public MessageResponse delaySend(String topic, String tag, int delay, int timeout, Message payLoad) {
        return doSend(topic, tag, payLoad, timeout, delay);
    }

    @Override
    public MessageResponse send(String topic, Message payLoad) {
        return doSend(topic, null, payLoad, mqConfiguration.getSendMessageTimeout(), 0);
    }

    @Override
    public MessageResponse send(String topic, String tag, Message payLoad) {
        return doSend(topic, tag, payLoad, mqConfiguration.getSendMessageTimeout(), 0);
    }

    @Override
    public void asyncSend(String topic, Message payLoad) {
        doAsyncSend(topic, null, payLoad, null, 0, 0);
    }

    @Override
    public void asyncSend(String topic, Message payLoad, int timeout) {
        doAsyncSend(topic, null, payLoad, null, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, Message payLoad) {
        doAsyncSend(topic, tag, payLoad, null, 0, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, Message payLoad, int timeout) {
        doAsyncSend(topic, tag, payLoad, null, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, Message payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, 0, 0);
    }

    @Override
    public void asyncSend(String topic, Message payLoad, int timeout, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, Message payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, 0, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, Message payLoad, int timeout, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, timeout, 0);
    }

    @Override
    public void delayAsyncSend(String topic, Message payLoad, int delay, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, 0, delay);
    }

    @Override
    public void delayAsyncSend(String topic, Message payLoad, int delay, int timeout, MessageSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, timeout, delay);
    }

    @Override
    public void delayAsyncSend(String topic, String tag, int delay, Message payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, 0, delay);
    }

    @Override
    public void delayAsyncSend(String topic, String tag, int delay, int timeout, Message payLoad, MessageSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, timeout, delay);
    }

    private MessageResponse doSend(String topic, String tag, Message payLoad, long timeout, int delayLevel) {
        String sendId = messageIdGenerator.getId();
        if (timeout <= 0) {
            timeout = mqConfiguration.getSendMessageTimeout();
        }
        payLoad.setSendId(sendId);
        try {
            P message = buildMessage(topic, tag, delayLevel, payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] start send message sendId = {} topic = {} tag = {} payLoad = {} ",
                        sendId, topic, tag, JSON.toJSONString(message));
            }
            R result = executeSend(topic, tag, message, timeout, delayLevel);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] send message finish sendId = {} payLoad = {}", sendId, JSON.toJSONString(message));
            }
            return convertToMQResponse(result, sendId);
        } catch (Exception e) {
            log.error("[MQ] send message error sendId:" + sendId, e);
            throw new MQException(e);
        }
    }

    private void doAsyncSend(String topic, String tag, Message payLoad, MessageSendCallback callback, long timeout, int delayLevel) {
        String sendId = messageIdGenerator.getId();
        if (timeout <= 0) {
            timeout = mqConfiguration.getSendMessageTimeout();
        }
        payLoad.setSendId(sendId);
        try {
            P message = buildMessage(topic, tag, delayLevel, payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] start send message sendId = {} topic = {} tag = {} message = {} ",
                        sendId, topic, tag, JSON.toJSONString(message));
            }
            executeAsyncSend(topic, tag, message, timeout, delayLevel, new MessageSendCallback() {
                @Override
                public void onSuccess(MessageResponse messageResponse) {
                    if (log.isDebugEnabled()) {
                        log.debug("[MQ] send message success, sendId = {} topic = {} tag = {} message = {} ",
                                sendId, topic, tag, JSON.toJSONString(message));
                    }
                    if (callback != null) {
                        callback.onSuccess(messageResponse);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[MQ] send message error", throwable);
                    if (callback != null) {
                        callback.onException(throwable);
                    }
                }
            }, sendId);
        } catch (Exception e) {
            log.error("[MQ] send message error", e);
            throw new MQException(e);
        }
    }

    /**
     * MQ实现构造自己的消息体
     */
    protected abstract P buildMessage(String topic, String tag, int delayLevel, Message message);

    /**
     * 执行同步发送
     */
    protected abstract R executeSend(String destination, String tag, P messagePayLoad, long timeout, int delayLevel);

    /**
     * 执行异步发送，通过callback接收发送结果
     */
    protected abstract void executeAsyncSend(String topic, String tag, P body, long timeout, int delayLevel, MessageSendCallback callback, String sendId);

    /**
     * 转换回统一的MQ响应体
     */
    protected abstract MessageResponse convertToMQResponse(R sendResult, String sendId);

}
