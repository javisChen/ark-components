package com.kt.component.lock.redis;

import com.kt.component.lock.LockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLockService implements LockService {

    private final RedissonClient redissonClient;

    public RedisLockService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            log.error("tryLock error", e);
            return false;
        }
    }

    @Override
    public boolean lock(String key, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(leaseTime, unit);
        } catch (InterruptedException e) {
            log.error("tryLock error", e);
            return false;
        }
    }

    @Override
    public boolean unlock(String key) {
        try {
            redissonClient.getLock(key).unlock();
        } catch (Exception e) {
            log.error("unlock error", e);
            return false;
        }
        return true;
    }
}
