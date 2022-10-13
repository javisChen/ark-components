package com.ark.component.lock.redis;

import com.ark.component.lock.LockService;
import com.ark.component.lock.exception.LockException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

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
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    public boolean lock(String key, long leaseTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(leaseTime, unit);
        } catch (Exception e) {
            throw new LockException(e);
        }
    }

    @Override
    public void unlock(String key) {
        try {
            redissonClient.getLock(key).unlock();
        } catch (Exception e) {
            throw new LockException(e);
        }
    }
}
