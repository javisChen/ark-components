package com.ark.component.mq.rabbit.support;

import cn.hutool.core.util.StrUtil;
import com.ark.component.mq.rabbit.configuation.RabbitMQConfiguration;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
public class ConnectionManager {

    private final static ConnectionFactory connectionFactory = new ConnectionFactory();

    private static volatile Connection connection;

    private static final Object lock = new Object();


    private ConnectionManager() {}

    public static Connection getConnection(RabbitMQConfiguration rabbitMQConfiguration) throws IOException, TimeoutException {
        if (connection != null) {
            log.info("[RabbitMQ]:已连接服务器 {}", connection.getAddress());
            return connection;
        }
        synchronized (lock) {
            if (connection == null) {
                List<Address> addresses = resolveAddresses(rabbitMQConfiguration);
                connection = connectionFactory.newConnection(addresses);
                log.info("[RabbitMQ]:连接服务器成功 {}", connection.getAddress());
            }
        }
        return connection;
    }


    /**
     * 解析服务端集群地址
     */
    private static List<Address> resolveAddresses(RabbitMQConfiguration rabbitMQConfiguration) {
        return StrUtil.split(rabbitMQConfiguration.getServer(), ",", true, true).stream()
                .map(Address::parseAddress)
                .collect(Collectors.toList());
    }
}
