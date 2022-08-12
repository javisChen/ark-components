package com.kt.component.mq;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class MessageResponse {

    private String msgId;

}
