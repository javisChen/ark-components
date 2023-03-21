//package com.ark.component.mq.integration.rabbit.raw;
//
//import com.ark.component.mq.integration.MQTestConst;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//@Slf4j
//public class RabbitListener {
//
//    @RabbitHandler
//    @org.springframework.amqp.rabbit.annotation.RabbitListener(
//            bindings = {
//                    @QueueBinding(
//                            value = @Queue("order_order_created_001"),
//                            exchange = @Exchange(name = MQTestConst.TOPIC_ORDER, type = "fanout")
//                    )
//            }
//    )
//    public void receive(String message, Message message1, Channel channel) {
//        try {
//            log.info("fanout test.... {}", message);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        } finally {
//            long deliverTag = message1.getMessageProperties().getDeliveryTag();
//            try {
//                channel.basicAck(deliverTag, false);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
