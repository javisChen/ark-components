package com.ark.component.lock.redis;

import com.ark.component.lock.LockService;
import com.ark.component.lock.exception.LockException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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
            log.info("Trying to acquire lock: {}, waitTime: {}, leaseTime: {}, timeUnit: {}", key, waitTime, leaseTime, timeUnit);
            boolean acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (acquired) {
                log.info("Successfully acquired lock: {}", key);
            } else {
                log.info("Failed to acquire lock: {}", key);
            }
            return acquired;
        } catch (Exception e) {
            log.error("Error while trying to acquire lock: {}", key, e);
            throw new LockException(e);
        }
    }

    @Override
    public <T> T tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> callback) {
        boolean locked = false;
        try {
            log.info("Trying to acquire lock with callback: {}, waitTime: {}, leaseTime: {}, timeUnit: {}", key, waitTime, leaseTime, timeUnit);
            locked = tryLock(key, waitTime, leaseTime, timeUnit);
            if (locked) {
                log.info("Executing callback for lock: {}", key);
                return callback.get();
            }
            log.info("Skipping callback execution, failed to acquire lock: {}", key);
            return null;
        } finally {
            if (locked) {
                log.info("Auto-releasing lock after callback: {}", key);
                unlock(key);
            }
        }
    }

    @Override
    public void tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Runnable callback) {
        boolean locked = false;
        try {
            log.info("Trying to acquire lock with runnable: {}, waitTime: {}, leaseTime: {}, timeUnit: {}", key, waitTime, leaseTime, timeUnit);
            locked = tryLock(key, waitTime, leaseTime, timeUnit);
            if (locked) {
                log.info("Executing runnable for lock: {}", key);
                callback.run();
            } else {
                log.info("Skipping runnable execution, failed to acquire lock: {}", key);
            }
        } finally {
            if (locked) {
                log.info("Auto-releasing lock after runnable: {}", key);
                unlock(key);
            }
        }
    }

    @Override
    public boolean lock(String key, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(key);
        try {
            log.info("Trying to acquire lock immediately: {}, leaseTime: {}, timeUnit: {}", key, leaseTime, timeUnit);
            boolean acquired = lock.tryLock(leaseTime, timeUnit);
            if (acquired) {
                log.info("Successfully acquired immediate lock: {}", key);
            } else {
                log.info("Failed to acquire immediate lock: {}", key);
            }
            return acquired;
        } catch (Exception e) {
            log.error("Error while trying to acquire immediate lock: {}", key, e);
            throw new LockException(e);
        }
    }

    @Override
    public <T> T lock(String key, long leaseTime, TimeUnit timeUnit, Supplier<T> callback) {
        boolean locked = false;
        try {
            log.info("Trying to acquire immediate lock with callback: {}, leaseTime: {}, timeUnit: {}", key, leaseTime, timeUnit);
            locked = lock(key, leaseTime, timeUnit);
            if (locked) {
                log.info("Executing callback for immediate lock: {}", key);
                return callback.get();
            }
            log.info("Skipping callback execution, failed to acquire immediate lock: {}", key);
            return null;
        } finally {
            if (locked) {
                log.info("Auto-releasing immediate lock after callback: {}", key);
                unlock(key);
            }
        }
    }

    @Override
    public void lock(String key, long leaseTime, TimeUnit timeUnit, Runnable callback) {
        boolean locked = false;
        try {
            log.info("Trying to acquire immediate lock with runnable: {}, leaseTime: {}, timeUnit: {}", key, leaseTime, timeUnit);
            locked = lock(key, leaseTime, timeUnit);
            if (locked) {
                log.info("Executing runnable for immediate lock: {}", key);
                callback.run();
            } else {
                log.info("Skipping runnable execution, failed to acquire immediate lock: {}", key);
            }
        } finally {
            if (locked) {
                log.info("Auto-releasing immediate lock after runnable: {}", key);
                unlock(key);
            }
        }
    }

    @Override
    public void unlock(String key) {
        try {
            log.info("Releasing lock: {}", key);
            RLock lock = redissonClient.getLock(key);
            lock.unlock();
            log.info("Successfully released lock: {}", key);
        } catch (Exception e) {
            log.error("Error while releasing lock: {}", key, e);
            // 如果锁不存在也会抛异常
            throw new LockException(e);
        }
    }
}
