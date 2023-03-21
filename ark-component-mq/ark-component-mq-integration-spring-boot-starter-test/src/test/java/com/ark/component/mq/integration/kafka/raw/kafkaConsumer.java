package com.ark.component.mq.integration.kafka.raw;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class kafkaConsumer {

    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<>();
        // 设置连接Kafka的初始连接用到的服务器地址
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://127.0.0.1:9092");
        // 设置key的序列化类
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        // 设置value的序列化类
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        configs.put(ProducerConfig.ACKS_CONFIG, "all");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList("test_topic_1"));
        consumer.poll()
    }
}
