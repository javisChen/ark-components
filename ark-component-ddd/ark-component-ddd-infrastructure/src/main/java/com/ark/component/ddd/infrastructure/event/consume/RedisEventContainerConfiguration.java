//package com.ark.ddd.infrastructure.event.consume;
//
//
//import com.mryqr.core.common.properties.MryRedisProperties;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.context.annotation.Profile;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.stream.ObjectRecord;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
//import org.springframework.util.ErrorHandler;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.stream.IntStream;
//
//import static com.mryqr.core.common.utils.MryConstants.REDIS_DOMAIN_EVENT_CONSUMER_GROUP;
//import static org.springframework.data.redis.connection.stream.Consumer.from;
//import static org.springframework.data.redis.connection.stream.ReadOffset.lastConsumed;
//import static org.springframework.data.redis.connection.stream.StreamOffset.create;
//
//@Slf4j
//@Profile("!ci")
//@Configuration
//@DependsOn("redisStreamInitializer")
//@RequiredArgsConstructor
//public class RedisEventContainerConfiguration {
//    private final MryRedisProperties mryRedisProperties;
//    private final DomainEventListener domainEventListener;
//
//    @Qualifier("consumeDomainEventTaskExecutor")
//    private final TaskExecutor consumeDomainEventTaskExecutor;
//
//    @Bean
//    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> domainEventContainer(RedisConnectionFactory factory) {
//        var options = StreamMessageListenerContainerOptions
//                .builder()
//                .batchSize(10)
//                .executor(consumeDomainEventTaskExecutor)
//                .targetType(String.class)
//                .errorHandler(new MryRedisErrorHandler())
//                .build();
//
//        var container = StreamMessageListenerContainer.create(factory, options);
//
//        IntStream.range(1, 26).forEach(index -> {//25个consumer，对应25个线程，每个consumer线程负责拉取消息并处理
//            try {
//                container.receiveAutoAck(
//                        from(REDIS_DOMAIN_EVENT_CONSUMER_GROUP, InetAddress.getLocalHost().getHostName() + "-" + index),
//                        create(mryRedisProperties.getDomainEventStream(), lastConsumed()),
//                        domainEventListener);
//            } catch (UnknownHostException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        container.start();
//        return container;
//    }
//
//    @Slf4j
//    private static class MryRedisErrorHandler implements ErrorHandler {
//        @Override
//        public void handleError(Throwable t) {
//            log.error(t.getMessage());
//        }
//    }
//
//}
//
