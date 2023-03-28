package com.ark.component.mq.integration.kafka.raw;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class kafkaConsumer {

    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<>();
        // 设置连接Kafka的初始连接用到的服务器地址
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://127.0.0.1:9092");
        // 设置key的序列化类
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 设置value的序列化类
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group_01");
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, "client_01");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        KafkaConsumer<String, String> consumer = null;
        try {
            consumer = new KafkaConsumer<>(configs);
            consumer.subscribe(List.of("test_topic_1"));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("consume record：" + record);
                }
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (consumer != null) {
                consumer.close();
            }
        }
    }
}
