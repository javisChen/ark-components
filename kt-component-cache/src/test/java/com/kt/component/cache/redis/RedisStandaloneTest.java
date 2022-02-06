package com.kt.component.cache.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("redis-standalone")
class RedisStandaloneTest extends ApplicationTests {

    private RedisService redisService;

    @Autowired
    private RedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        redisService = new RedisService(redisTemplate);
    }

    @Test
    void set() {
        boolean set = redisService.set("name", "kk");
        Assertions.assertTrue(set);
    }

    @Test
    void testSet() {
    }

    @Test
    void remove() {
    }

    @Test
    void removePattern() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void exists() {
    }

    @Test
    void get() {
        Object set = redisService.get("name");
        System.out.println(set);
    }

    @Test
    void hmSet() {
    }

    @Test
    void hmGet() {
    }

    @Test
    void lPush() {
    }

    @Test
    void lRange() {
    }

    @Test
    void setArray() {
    }

    @Test
    void getArray() {
    }

    @Test
    void zAdd() {
    }

    @Test
    void rangeByScore() {
    }
}