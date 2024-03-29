package com.ark.component.lock.redis.autoconfigure;

import com.ark.component.lock.LockService;
import com.ark.component.lock.redis.RedisLockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
public class RedisLockAutoConfiguration {

    public RedisLockAutoConfiguration() {
        log.info("enable [ark-component-lock-redis-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public LockService lockService(RedissonClient redissonClient) {
        return new RedisLockService(redissonClient);
    }

}
