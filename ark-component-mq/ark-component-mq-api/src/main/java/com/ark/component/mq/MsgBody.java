package com.ark.component.mq;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MsgBody {

    private Object body;

    /**
     * 组件自生成的Id
     */
    private String sendId;

    public MsgBody(Object body) {
        this.body = body;
    }

    public MsgBody(String sendId, Object body) {
        this.body = body;
        this.sendId = sendId;
    }

    public static MsgBody of(Object body) {
        return new MsgBody(body);
    }
}
