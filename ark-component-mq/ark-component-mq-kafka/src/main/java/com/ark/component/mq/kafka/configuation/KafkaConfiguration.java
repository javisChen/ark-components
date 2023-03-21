package com.ark.component.mq.kafka.configuation;

import com.ark.component.mq.configuation.MQConfiguration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ配置
 * @author jc
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ConfigurationProperties(prefix = "ark.component.mq.kafka")
public class KafkaConfiguration extends MQConfiguration {

    /**
     * 发送消息超时时间，默认3s
     */
    private Producer producer;

    private Consumer consumer;

    @Data
    public static class Producer {
        private Boolean enabled = false;
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
        private Boolean enabled = false;
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
