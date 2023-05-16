package com.ark.component.mq.rabbit.support;

import com.ark.component.mq.SendConfirm;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 发送确认的回调管理器
 * todo 需要加上过期自动清理，否则消息一直没确认的话会出现内存泄露
 * todo 存储到Redis
 */
public class ConfirmManager {

    /**
     * 存储需要通知发送成功事件
     * key=MessageId+CorrelationId,value是回调钩子
     */
    private static final Map<String, SendConfirm> PENDING_CONFIRMS = new ConcurrentHashMap<>();

    public static void put(String messageId, String correlationId, SendConfirm sendCallback) {
        PENDING_CONFIRMS.put(buildKey(messageId, correlationId), sendCallback);
    }

    public static SendConfirm get(String messageId, String correlationId) {
        if (ObjectUtils.isEmpty(messageId) || ObjectUtils.isEmpty(correlationId)) {
            return null;
        }
        return PENDING_CONFIRMS.get(buildKey(messageId, correlationId));
    }

    public static void remove(String messageId, String correlationId) {
        PENDING_CONFIRMS.remove(buildKey(messageId, correlationId));
    }

    private static String buildKey(String messageId, String correlationId) {
        return messageId + "-" + correlationId;
    }

}
