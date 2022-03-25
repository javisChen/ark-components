package com.kt.component.lock.redis;

import com.kt.component.lock.LockService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("redis-standalone")
class RedisLockTest extends ApplicationTests {

    private LockService lockService;

    private static int count = 10;


    @Autowired
    private RedissonClient redissonClient;

    @BeforeEach
    public void setup() {
        lockService = new RedisLockService(redissonClient);
    }

    @Test
    void testLock() {
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        for (int i = 0; i < 300; i++) {
            executorService.execute(() -> {
                boolean lock = false;
                try {
                    lock = lockService.tryLock("lock", 30, 30, TimeUnit.SECONDS);
                    if (lock) {
                        if (count > 0) {
                            count--;
                            System.out.println(Thread.currentThread().getId() + "抢占锁成功 -> "  + count);
                        }
                    } else {
                        System.out.println(Thread.currentThread().getId() + "抢占锁失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (lock) {
                        System.out.println(Thread.currentThread().getId() + "释放锁");
                        lockService.unlock("lock");
                    }
                }
            });
        }
        while (count > 0) {
//            System.out.println();
        }
        System.out.println("库存清空了");
//        Assertions.assertTrue(lock);
    }

    @Test
    void testUnlock() {
        lockService.unlock("lock");
        Assertions.assertTrue(true);
    }
}