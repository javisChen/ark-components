package com.kt.tests;

import com.kt.component.cache.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Application.class})
public class ApplicationTests {

	@Autowired
	private RedisService redisService;

	@Test
	void contextLoads() {

	}

}
