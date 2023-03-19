//package com.ark.component.mq.integration.rabbit;
//
//import com.ark.component.mq.rabbit.support.Utils;
//import com.rabbitmq.client.BuiltinExchangeType;
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitDeclareConfig {
//
//    @Bean
//    public FanoutExchange orderExchange() {
//        return new FanoutExchange("order", true, false, null);
//    }
//
//    //创建队列A
//    @Bean
//    public Queue fanoutQueue(){
////        String queueName = Utils.createQueueName("order", "order_created", BuiltinExchangeType.FANOUT);
//        String queueName = "order_order_created_001";
//        return new Queue(queueName,false,true,true);
//    }
//
//    //将创建的队列绑定到创建的交换机上
//    @Bean
//    public Binding bindingA(Queue queue){
//        return BindingBuilder.bind(queue).to(orderExchange());
//    }
//}
