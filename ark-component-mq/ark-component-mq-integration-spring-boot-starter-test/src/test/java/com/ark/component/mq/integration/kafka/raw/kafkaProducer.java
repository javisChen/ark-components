package com.ark.component.mq.integration.kafka.raw;

import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.Map;

public class kafkaProducer {

    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<>();
        // 设置连接Kafka的初始连接用到的服务器地址
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "http://127.0.0.1:9092");
        // 设置key的序列化类
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerSerializer");
        // 设置value的序列化类
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        KafkaProducer<Integer, String> kafkaProducer = new KafkaProducer<Integer, String>(configs);
        //发送100条消息
        for (int i = 0; i < 10; i++) {
            ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>
                    ("test_topic_1",
                            0,
                            i,
                            "test topic msg " + i);
            //消息的异步确认
            kafkaProducer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                    if (exception == null) {
                        System.out.println("消息的主题：" + recordMetadata.topic());
                        System.out.println("消息的分区：" + recordMetadata.partition());
                        System.out.println("消息的偏移量：" + recordMetadata.offset());
                    } else {
                        System.out.println("发送消息异常");
                    }
                }
            });

        }
        // 关闭生产者
        kafkaProducer.close();
    }
}
