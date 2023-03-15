package com.ark.component.mq.rabbit.support;

import cn.hutool.core.util.RandomUtil;
import com.rabbitmq.client.BuiltinExchangeType;

public class Utils {

    public static String createQueueName(String exchange, String routingKey, BuiltinExchangeType exchangeType) {
        String queue = exchange + "_" + routingKey;
        // 如果交换器是Fanout类型，就给队列名加上随机字符串
        if (exchangeType.equals(BuiltinExchangeType.FANOUT)) {
            queue += genRandomQueueSuffix();
        }
        return queue;
    }

    private static String genRandomQueueSuffix() {
        return "_" + RandomUtil.randomString(16) + "_" + System.currentTimeMillis();
    }

}
