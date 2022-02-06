package com.kt.component.lock;

import java.util.concurrent.TimeUnit;

public interface LockService {

    boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit unit);

    boolean lock(String key, long leaseTime, TimeUnit unit);

    boolean unlock(String key);
}
