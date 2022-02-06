package com.kt.component.cache.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("redis-cluster")
class RedisClusterTest extends ApplicationTests {

    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        redisService = new RedisService(redisTemplate);
    }

    @Test
    void testSet() {
        boolean set = redisService.set("name", "6666");
        Assertions.assertTrue(set);
    }

    @Test
    void testGet() {
        Object set = redisService.get("name");
        System.out.println(set);
    }
}