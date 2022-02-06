package com.kt.component.cache.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("redis-sentinel")
class RedisSentinelTest extends ApplicationTests {

    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        redisService = new RedisService(redisTemplate);
    }

    @Test
    void set() {
        boolean set = redisService.set("name", "kk123");
        Assertions.assertTrue(set);
    }

    @Test
    void get() {
        Object set = redisService.get("name");
        System.out.println(set);
    }
}