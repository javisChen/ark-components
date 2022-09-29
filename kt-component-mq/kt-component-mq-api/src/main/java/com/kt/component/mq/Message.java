package com.kt.component.mq;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message {

    private Object body;
    private String msgId;

    public Message(Object body) {
        this.body = body;
    }

    public Message(String msgId, Object body) {
        this.body = body;
        this.msgId = msgId;
    }

    public static Message of(Object body) {
        return new Message(body);
    }
}
