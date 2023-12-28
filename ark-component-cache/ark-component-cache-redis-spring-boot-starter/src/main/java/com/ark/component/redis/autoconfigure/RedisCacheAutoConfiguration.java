package com.ark.component.redis.autoconfigure;

import com.alibaba.fastjson2.support.spring6.data.redis.FastJsonRedisSerializer;
import com.ark.component.cache.redis.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
public class RedisCacheAutoConfiguration {

    public RedisCacheAutoConfiguration() {
        log.info("enable [ark-component-cache-redis-spring-boot-starter]");
    }

    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key value 采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(fastJsonRedisSerializer);
        // hash的key value 也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisCacheService")
    public RedisCacheService redisService(RedisConnectionFactory factory) {
        return new RedisCacheService(redisTemplate(factory));
    }
}
