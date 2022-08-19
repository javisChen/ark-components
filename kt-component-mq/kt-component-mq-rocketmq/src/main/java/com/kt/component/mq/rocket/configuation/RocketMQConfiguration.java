package com.kt.component.mq.rocket.configuation;

import com.kt.component.mq.configuation.MQConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ配置
 * @author jc
 */
@Data
@ConfigurationProperties(prefix = "kt.mq.rocketmq")
public class RocketMQConfiguration extends MQConfiguration {

    /**
     * 发送消息超时时间，默认3s
     */
    private Producer producer;

    private Consumer consumer;

    @Data
    public static class Producer {
        private String group;
        private int sendMessageTimeout = 30000;
        private int compressMessageBodyThreshold = 4096;
        private int retryTimesWhenSendFailed = 2;
        private int retryTimesWhenSendAsyncFailed = 2;
        private boolean retryNextServer = false;
        private int maxMessageSize = 4194304;
        private String accessKey;
        private String secretKey;
        private boolean enableMsgTrace = false;
        private String customizedTraceTopic = "RMQ_SYS_TRACE_TOPIC";
    }

    @Data
    public static final class Consumer {
        private String group;
        private String topic;
        private String messageModel = "CLUSTERING";
        private String selectorType = "TAG";
        private String selectorExpression = "*";
        private String accessKey;
        private String secretKey;
        private int pullBatchSize = 10;
        private boolean enableMsgTrace = false;
        private String customizedTraceTopic = "RMQ_SYS_TRACE_TOPIC";
        private Map<String, Map<String, Boolean>> listeners = new HashMap();
    }

}
