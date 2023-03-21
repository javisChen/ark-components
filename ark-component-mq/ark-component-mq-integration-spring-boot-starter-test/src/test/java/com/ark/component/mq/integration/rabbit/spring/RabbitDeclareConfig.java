//package com.ark.component.mq.integration.rabbit;
//
//import com.ark.component.mq.rabbit.support.Utils;
//import com.rabbitmq.client.BuiltinExchangeType;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration(proxyBeanMethods = false)
//@Slf4j
//public class RabbitDeclareConfig {
//
//    @Bean
//    public FanoutExchange orderExchange() {
//        return new FanoutExchange("order", true, false, null);
//    }
//
//    //创建队列A
////    @Bean
////    public Queue fanoutQueue(){
////        String queueName = Utils.createQueueName("order", "order_created", BuiltinExchangeType.FANOUT);
//////        String queueName = "order_order_created_001";
////        return new AnonymousQueue(new NamingStrategy() {
////            @Override
////            public String generateName() {
////                return Utils.createQueueName("order", "order_created", BuiltinExchangeType.FANOUT);
////            }
////        });
////    }
//
//    //将创建的队列绑定到创建的交换机上
//    @Bean
//    public Binding binding(Queue queue, FanoutExchange orderExchange) {
//        return BindingBuilder.bind(queue).to(orderExchange);
//    }
//}
