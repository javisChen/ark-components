package com.kt.component.lock.exception;

/**
 * 分布式锁操作异常类
 * @author victor
 */
public class LockException extends RuntimeException {

    public LockException(Throwable cause) {
        super(cause);
    }
}
