package com.ark.component.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁统一接口
 */
public interface LockService {

    /**
     * 尝试抢占锁，抢占失败会等待waitTime
     *
     * @param key       锁key
     * @param waitTime  尝试获取锁，获取不到就等会等待waitTime
     * @param leaseTime 锁最长持有的时间，当业务在leaseTime时长内没有执行完，会强制释放锁。可以设置为-1，必须要手动解锁。
     * @param timeUnit  时间单位
     * @return 抢锁成功返回true，否则返回false
     */
    boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit);

    /**
     * 尝试抢占锁，抢锁失败直接返回false
     *
     * @param key       锁key
     * @param leaseTime 锁最长持有的时间，当业务在leaseTime时长内没有执行完，会强制释放锁。可以设置为-1，必须要手动解锁。
     * @param timeUnit  时间单位
     * @return 抢锁成功返回true，否则返回false
     */
    boolean lock(String key, long leaseTime, TimeUnit timeUnit);

    /**
     * 解锁
     *
     * @param key 锁key
     */
    void unlock(String key);
}
