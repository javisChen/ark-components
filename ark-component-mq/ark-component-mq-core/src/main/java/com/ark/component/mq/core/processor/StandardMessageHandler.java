package com.ark.component.mq.core.processor;

import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson2.JSON;
import com.ark.component.common.id.TraceIdUtils;
import com.ark.component.mq.MsgBody;
import com.ark.component.mq.core.serializer.MessageSerializer;
import com.ark.component.mq.exception.MQException;
import com.ark.component.mq.exception.MQSerializerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import static com.ark.component.context.core.contants.ContextConstants.HEADER_TRACE_ID;

/**
 * 一个处理消息的框架。极大多数情况，只需要继承当前Processor
 * 1.消息反序列化
 * 2.消息幂等校验
 * 3.消息处理
 * @param <T>
 */
@Slf4j
@SuppressWarnings("all")
public abstract class StandardMessageHandler<T, RAW> implements MessageHandler<RAW>, ApplicationContextAware {

    private MessageSerializer messageSerializer;

    @Override
    public void handle(byte[] body, String msgId, RAW raw) throws MQException {
        if (log.isDebugEnabled()) {
            log.debug("Recevied Message: msgId = {}, bodySize = {}", msgId, body.length);
        }
        MsgBody message;
        T msgBody;
        try {
            message = messageSerializer.deserialize(body, MsgBody.class);
            String reqId = message.getTraceId();
            MDC.put(HEADER_TRACE_ID, StringUtils.defaultIfBlank(reqId, TraceIdUtils.getId()));
            if (log.isDebugEnabled()) {
                log.debug("Message {} has been successfully deserialized, content = [{}].", msgId, JSON.toJSONString(message));
            }
            msgBody = parseMsgBody(message);
        } catch (MQSerializerException e) {
            log.error("Message {} failed to be deserialized.", msgId, e);
            throw new MQSerializerException(e);
        }
        String bizKey = message.getBizKey();
        try {
            // todo 消费幂等校验，这里还没实现
            if (isRepeatMessage(msgId, bizKey, msgBody, raw)) {
                if (log.isDebugEnabled()) {
                    log.debug("Message {} has already been processed before.", msgId);
                }
                return;
            }
            handleMessage(msgId, bizKey, msgBody, raw);
            if (log.isDebugEnabled()) {
                log.debug("Message {} has been successfully processed", msgId);
            }
        } catch (Exception e) {
            log.error("Message {} prcessing failed", msgId, e);
            throw new MQException(e);
        }
    }

    private T parseMsgBody(MsgBody msgBody) {
        return JSON.parseObject(JSON.toJSONString(msgBody.getBody()), TypeUtil.getTypeArgument(getClass()));
    }

    protected abstract void handleMessage(String msgId, String bizKey, T body, RAW raw);

    protected boolean isRepeatMessage(String msgId, String bizKey, T body, RAW raw) {
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setMessageCodec(applicationContext.getBean(MessageSerializer.class));
    }

    public void setMessageCodec(MessageSerializer messageSerializer) {
        this.messageSerializer = messageSerializer;
    }
}
