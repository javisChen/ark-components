package com.ark.component.mq.core;

import com.alibaba.fastjson.JSON;
import com.ark.component.mq.MQService;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.MQSendResponse;
import com.ark.component.mq.MQSendCallback;
import com.ark.component.mq.configuation.MQConfiguration;
import com.ark.component.mq.core.generator.DefaultMsgIdGenerator;
import com.ark.component.mq.core.generator.MsgIdGenerator;
import com.ark.component.mq.exception.MQException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

/**
 *
 * @param <P> MQ发送对象
 * @param <R> MQ发送响应对象
 */
@Slf4j
public abstract class AbstractMQService<P, R> implements MQService, ApplicationContextAware {
    private final MQConfiguration mqConfiguration;

    private MsgIdGenerator msgIdGenerator = new DefaultMsgIdGenerator();

    protected AbstractMQService(MQConfiguration mqConfiguration) {
        this.mqConfiguration = mqConfiguration;
    }

    protected AbstractMQService() {
        this.mqConfiguration = new MQConfiguration();
    }

    public MsgIdGenerator getMsgIdGenerator() {
        return msgIdGenerator;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.msgIdGenerator = applicationContext.getBean(MsgIdGenerator.class);
    }

    @Override
    public MQSendResponse send(String topic, MsgBody payLoad, int timeout) {
        return doSend(topic, null, payLoad, timeout, 0);
    }

    @Override
    public MQSendResponse send(String topic, String tag, MsgBody payLoad, int timeout) {
        return doSend(topic, null, payLoad, timeout, 0);
    }

    @Override
    public MQSendResponse delaySend(String topic, MsgBody payLoad, int delay) {
        return doSend(topic, null, payLoad, 0, delay);
    }

    @Override
    public MQSendResponse delaySend(String topic, MsgBody payLoad, int delay, int timeout) {
        return doSend(topic, null, payLoad, timeout, delay);
    }

    @Override
    public MQSendResponse delaySend(String topic, String tag, int delay, MsgBody payLoad) {
        return doSend(topic, tag, payLoad, 0, delay);
    }

    @Override
    public MQSendResponse delaySend(String topic, String tag, int delay, int timeout, MsgBody payLoad) {
        return doSend(topic, tag, payLoad, timeout, delay);
    }

    @Override
    public MQSendResponse send(String topic, MsgBody payLoad) {
        return doSend(topic, null, payLoad, mqConfiguration.getSendMessageTimeout(), 0);
    }

    @Override
    public MQSendResponse send(String topic, String tag, MsgBody payLoad) {
        return doSend(topic, tag, payLoad, mqConfiguration.getSendMessageTimeout(), 0);
    }

    @Override
    public void asyncSend(String topic, MsgBody payLoad) {
        doAsyncSend(topic, null, payLoad, null, 0, 0);
    }

    @Override
    public void asyncSend(String topic, MsgBody payLoad, int timeout) {
        doAsyncSend(topic, null, payLoad, null, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MsgBody payLoad) {
        doAsyncSend(topic, tag, payLoad, null, 0, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MsgBody payLoad, int timeout) {
        doAsyncSend(topic, tag, payLoad, null, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, MsgBody payLoad, MQSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, 0, 0);
    }

    @Override
    public void asyncSend(String topic, MsgBody payLoad, int timeout, MQSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, timeout, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MsgBody payLoad, MQSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, 0, 0);
    }

    @Override
    public void asyncSend(String topic, String tag, MsgBody payLoad, int timeout, MQSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, timeout, 0);
    }

    @Override
    public void delayAsyncSend(String topic, MsgBody payLoad, int delay, MQSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, 0, delay);
    }

    @Override
    public void delayAsyncSend(String topic, MsgBody payLoad, int delay, int timeout, MQSendCallback callback) {
        doAsyncSend(topic, null, payLoad, callback, timeout, delay);
    }

    @Override
    public void delayAsyncSend(String topic, String tag, int delay, MsgBody payLoad, MQSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, 0, delay);
    }

    @Override
    public void delayAsyncSend(String topic, String tag, int delay, int timeout, MsgBody payLoad, MQSendCallback callback) {
        doAsyncSend(topic, tag, payLoad, callback, timeout, delay);
    }

    private MQSendResponse doSend(String topic, String tag, MsgBody payLoad, long timeout, int delayLevel) {
        String bizKey = buildBizKey(payLoad);
        payLoad.setBizKey(buildBizKey(payLoad));

        if (timeout <= 0) {
            timeout = mqConfiguration.getSendMessageTimeout();
        }
        try {
            P message = buildMessage(topic, tag, delayLevel, payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] start send message bizKey = {} topic = {} tag = {} payLoad = {} ",
                        bizKey, topic, tag, JSON.toJSONString(message));
            }
            R result = executeSend(bizKey, topic, tag, message, timeout, delayLevel);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] send message finish bizKey = {} payLoad = {}", bizKey, JSON.toJSONString(message));
            }
            return convertToMQResponse(result, bizKey);
        } catch (Exception e) {
            log.error("[MQ] send message error bizKey:" + bizKey, e);
            throw new MQException(e);
        }
    }

    private void doAsyncSend(String topic, String tag, MsgBody payLoad, MQSendCallback callback, long timeout, int delayLevel) {
        String bizKey = buildBizKey(payLoad);
        payLoad.setBizKey(bizKey);
        if (timeout <= 0) {
            timeout = mqConfiguration.getSendMessageTimeout();
        }
        try {
            P message = buildMessage(topic, tag, delayLevel, payLoad);
            if (log.isDebugEnabled()) {
                log.debug("[MQ] start send message bizKey = {} topic = {} tag = {} message = {} ",
                        bizKey, topic, tag, JSON.toJSONString(message));
            }
            executeAsyncSend(bizKey, topic, tag, message, timeout, delayLevel, new MQSendCallback() {
                @Override
                public void onSuccess(MQSendResponse MQSendResponse) {
                    if (log.isDebugEnabled()) {
                        log.debug("[MQ] send message success, bizKey = {} topic = {} tag = {} message = {} ",
                                bizKey, topic, tag, JSON.toJSONString(message));
                    }
                    if (callback != null) {
                        callback.onSuccess(MQSendResponse);
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    log.error("[MQ] send message error", throwable);
                    if (callback != null) {
                        callback.onException(throwable);
                    }
                }
            });
        } catch (Exception e) {
            log.error("[MQ] send message error", e);
            throw new MQException(e);
        }
    }

    private String buildBizKey(MsgBody payLoad) {
        String bizKey = payLoad.getBizKey();
        if (StringUtils.hasLength(bizKey)) {
            bizKey = msgIdGenerator.getId();
        }
        return bizKey;
    }

    /**
     * MQ实现构造自己的消息体
     */
    protected abstract P buildMessage(String topic, String tag, int delayLevel, MsgBody msgBody);

    /**
     * 执行同步发送
     */
    protected abstract R executeSend(String bizKey, String topic, String tag, P msgBody, long timeout, int delayLevel);

    /**
     * 执行异步发送，通过callback接收发送结果
     */
    protected abstract void executeAsyncSend(String bizKey, String topic, String tag, P body, long timeout, int delayLevel, MQSendCallback callback);

    /**
     * 转换回统一的MQ响应体
     */
    protected abstract MQSendResponse convertToMQResponse(R sendResult, String bizKey);

}
