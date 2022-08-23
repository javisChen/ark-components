package com.kt.component.mq.core;

import com.alibaba.fastjson.JSON;
import com.kt.component.mq.MQMessageService;
import com.kt.component.mq.MessagePayLoad;
import com.kt.component.mq.MessageResponse;
import com.kt.component.mq.MessageSendCallback;
import com.kt.component.mq.configuation.MQConfiguration;
import com.kt.component.mq.core.generator.MessageIdGenerator;
import com.kt.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
        return doSend(topic, null, payLoad, mqConfiguration.getSendMessageTimeout(), 0);
    }

    @Override
    public MessageResponse send(String topic, String tag, MessagePayLoad payLoad) {
        return doSend(topic, tag, payLoad, mqConfiguration.getSendMessageTimeout(), 0);
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

    private MessageResponse doSend(String topic, String tag, MessagePayLoad payLoad, long timeout, int delayLevel) {
        String msgId = messageIdGenerator.getId();
        if (timeout <= 0) {
            timeout = mqConfiguration.getSendMessageTimeout();
        }
        payLoad.setMsgId(msgId);
        try {
            P body = buildMessage(topic, tag, delayLevel, payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[mq] start send message msgId = {} topic = {} tag = {} payLoad = {} ",
                        msgId, topic, tag, JSON.toJSONString(body));
            }
            R result = executeSend(topic, tag, body, timeout, delayLevel);
            if (log.isDebugEnabled()) {
                log.debug("[mq] send message finish msgId = {} payLoad = {}", msgId, JSON.toJSONString(body));
            }
            return convertToMQResponse(result);
        } catch (Exception e) {
            log.error("[mq] send message error msgId:" + msgId, e);
            throw new MQException(e);
        }
    }

    private void doAsyncSend(String topic, String tag, MessagePayLoad payLoad, MessageSendCallback callback, long timeout, int delayLevel) {
        String msgId = messageIdGenerator.getId();
        if (timeout <= 0) {
            timeout = mqConfiguration.getSendMessageTimeout();
        }
        payLoad.setMsgId(msgId);
        try {
            P message = buildMessage(topic, tag, delayLevel, payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[mq] start send message msgId = {} topic = {} tag = {} message = {} ",
                        msgId, topic, tag, JSON.toJSONString(message));
            }
            executeAsyncSend(topic, tag, message, timeout, delayLevel, new MessageSendCallback() {
                @Override
                public void onSuccess(MessageResponse messageResponse) {
                    if (log.isDebugEnabled()) {
                        log.debug("[mq] start send message msgId:{} topic:{} tag:{} message:{} ",
                                msgId, topic, tag, JSON.toJSONString(message));
                    }
                    if (callback != null) {
                        callback.onSuccess(messageResponse);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[mq] send message error callback", throwable);
                    if (callback != null) {
                        callback.onException(throwable);
                    }
                }
            }, msgId);
        } catch (Exception e) {
            log.error("[mq] send message error", e);
            throw new MQException(e);
        }
    }

    /**
     * MQ实现构造自己的消息体
     */
    protected abstract P buildMessage(String topic, String tag, int delayLevel, MessagePayLoad messagePayLoad);

    /**
     * 执行同步发送
     */
    protected abstract R executeSend(String destination, String tag, P messagePayLoad, long timeout, int delayLevel);

    /**
     * 执行异步发送，通过callback接收发送结果
     */
    protected abstract void executeAsyncSend(String topic, String tag, P body, long timeout, int delayLevel, MessageSendCallback callback, String msgId);

    /**
     * 转换回统一的MQ响应体
     */
    protected abstract MessageResponse convertToMQResponse(R result);

}
