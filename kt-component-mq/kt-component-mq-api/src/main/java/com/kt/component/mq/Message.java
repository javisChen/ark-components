package com.kt.component.mq;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Message<T> {

    private T body;
    private String msgId;

    public Message(T body) {
        this.body = body;
    }

    public Message(String msgId, T body) {
        this.body = body;
        this.msgId = msgId;
    }
}
