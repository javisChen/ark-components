package com.ark.component.mq.rabbit.support;

public class Utils {

    public static String buildQueueName(String exchange, String queue) {
        return exchange + "_" + queue;
    }
}
