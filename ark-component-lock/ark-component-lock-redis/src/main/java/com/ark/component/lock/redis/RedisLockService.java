package com.ark.component.lock.redis;

import com.ark.component.lock.LockService;
import com.ark.component.lock.exception.LockException;
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
    public boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    public boolean lock(String key, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(leaseTime, timeUnit);
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    public void unlock(String key) {
        try {
            RLock lock = redissonClient.getLock(key);
            lock.unlock();
        } catch (Exception e) {
            // 如果锁不存在也会抛异常
            throw new LockException(e);
        }
    }
}
