package com.kt.component.mq;

import lombok.Data;

@Data
public class MessagePayLoad {

    private Object body;
    private String msgId;

    public MessagePayLoad(Object body) {
        this.body = body;
    }

    public MessagePayLoad(String msgId, Object body) {
        this.body = body;
        this.msgId = msgId;
    }
}
