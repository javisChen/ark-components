package com.kt.component.mq.configuation;

import lombok.Data;

/**
 * MQ配置
 *
 * @author jc
 */
@Data
public class MQConfiguration {

    private int sendMessageTimeout = 30000;

    private String server;

}
