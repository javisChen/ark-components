package com.ark.component.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

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
     * 尝试抢占锁，抢占失败会等待waitTime，成功后执行回调函数并自动解锁
     *
     * @param key       锁key
     * @param waitTime  尝试获取锁，获取不到就等会等待waitTime
     * @param leaseTime 锁最长持有的时间，当业务在leaseTime时长内没有执行完，会强制释放锁。可以设置为-1，必须要手动解锁。
     * @param timeUnit  时间单位
     * @param callback  获取锁成功后执行的回调函数
     * @param <T>       回调函数返回值类型
     * @return 回调函数的返回值，如果获取锁失败则返回null
     */
    <T> T tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> callback);

    /**
     * 尝试抢占锁，抢占失败会等待waitTime，成功后执行回调函数并自动解锁
     *
     * @param key       锁key
     * @param waitTime  尝试获取锁，获取不到就等会等待waitTime
     * @param leaseTime 锁最长持有的时间，当业务在leaseTime时长内没有执行完，会强制释放锁。可以设置为-1，必须要手动解锁。
     * @param timeUnit  时间单位
     * @param callback  获取锁成功后执行的回调函数
     */
    void tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Runnable callback);

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
     * 尝试抢占锁，抢锁失败直接返回false，成功后执行回调函数并自动解锁
     *
     * @param key       锁key
     * @param leaseTime 锁最长持有的时间，当业务在leaseTime时长内没有执行完，会强制释放锁。可以设置为-1，必须要手动解锁。
     * @param timeUnit  时间单位
     * @param callback  获取锁成功后执行的回调函数
     * @param <T>       回调函数返回值类型
     * @return 回调函数的返回值，如果获取锁失败则返回null
     */
    <T> T lock(String key, long leaseTime, TimeUnit timeUnit, Supplier<T> callback);

    /**
     * 尝试抢占锁，抢锁失败直接返回false，成功后执行回调函数并自动解锁
     *
     * @param key       锁key
     * @param leaseTime 锁最长持有的时间，当业务在leaseTime时长内没有执行完，会强制释放锁。可以设置为-1，必须要手动解锁。
     * @param timeUnit  时间单位
     * @param callback  获取锁成功后执行的回调函数
     */
    void lock(String key, long leaseTime, TimeUnit timeUnit, Runnable callback);

    /**
     * 解锁
     *
     * @param key 锁key
     */
    void unlock(String key);
}
