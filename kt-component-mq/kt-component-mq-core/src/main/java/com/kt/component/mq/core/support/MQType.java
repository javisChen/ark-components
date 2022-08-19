package com.kt.component.mq.core.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MQType {

    ROCKET("RocketMQ"),
    KAFKA("Kafka"),
    PULSAR("Pulsar"),
    RABBIT("RabbitMQ");

    private final String name;


}
