package com.ark.component.mq.configuation;

import lombok.Data;

/**
 * MQ配置
 *
 * @author jc
 */
@Data
public class MQConfiguration {

    /**
     * 发送消息超时时间，单位毫秒
     */
    private int sendMessageTimeout = 30000;

    /**
     * 服务端地址，可填写多个用逗号分隔：127.0.0:1:9876,127.0.0:1:9877
     */
    private String server;

    /**
     * 是否启用
     */
    private Boolean enabled;

}
