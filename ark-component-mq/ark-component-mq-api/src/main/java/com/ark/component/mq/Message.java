package com.ark.component.mq;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {

    private Object body;

    /**
     * 组件自生成的Id
     */
    private String sendId;

    public Message(Object body) {
        this.body = body;
    }

    public Message(String sendId, Object body) {
        this.body = body;
        this.sendId = sendId;
    }

    public static Message of(Object body) {
        return new Message(body);
    }
}
