package com.ark.component.mq;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MsgBody {

    /**
     * 消息存放内容
     */
    private Object body;

    /**
     * 业务标识
     */
    private String bizKey;

    /**
     * 业务标识
     */
    private String traceId;

    public MsgBody(Object body) {
        this.body = body;
    }

    public MsgBody(String bizKey, Object body) {
        this.body = body;
        this.bizKey = bizKey;
    }

    public static MsgBody of(Object body) {
        return new MsgBody(body);
    }

    public static MsgBody of(String traceId, Object body) {
        return new MsgBody(traceId, body);
    }
}
