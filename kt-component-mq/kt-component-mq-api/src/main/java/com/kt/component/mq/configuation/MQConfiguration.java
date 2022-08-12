package com.kt.component.mq.configuation;

import lombok.Data;

/**
 * MQ配置
 * @author jc
 */
@Data
public class MQConfiguration {

    /**
     * 发送消息超时时间，默认10s
     */
    private int timeout = 10;
}
