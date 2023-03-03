package com.ark.component.mq;

import lombok.Builder;
import lombok.Data;

/**
 * 消息发送响应结果
 */
@Data
@Builder(setterPrefix = "with")
public class MQSendResponse {

    private String msgId;

    private String sendId;

}
