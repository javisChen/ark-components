# ARK Lock Component

## 简介
ARK Lock Component 提供统一的分布式锁操作接口，支持多种锁实现（主要是Redis）。目前封装了基于Redisson的Redis分布式锁实现。

## 包名
- 接口定义: `com.ark.component.lock`

## 主要功能

### 锁操作
```
  // 尝试获取锁，可以等待一段时间
  boolean locked = lockService.tryLock("order:1001", 5, 30, TimeUnit.SECONDS);
  if (locked) {
    try {
      // 执行需要加锁的业务逻辑
      doSomething();
    } finally {
      // 释放锁
      lockService.unlock("order:1001");
    }
  }
  
  // 尝试获取锁，不等待（失败立即返回）
  boolean locked = lockService.lock("order:1001", 30, TimeUnit.SECONDS);
  if (locked) {
    try {
      // 执行需要加锁的业务逻辑
      doSomething();
    } finally {
      // 释放锁
      lockService.unlock("order:1001");
    }
  }
  
  // 释放锁
  lockService.unlock("order:1001");
  
  // 使用回调函数自动处理锁的获取和释放（有返回值）
  Order order = lockService.tryLock("order:1001", 5, 30, TimeUnit.SECONDS, () -> {
    // 在这里执行需要加锁的业务逻辑
    return orderService.getOrder("1001");
  });
  
  // 使用回调函数自动处理锁的获取和释放（无返回值）
  lockService.tryLock("order:1001", 5, 30, TimeUnit.SECONDS, () -> {
    // 在这里执行需要加锁的业务逻辑
    orderService.updateOrder("1001");
  });
  
  // 不等待的锁获取 + 回调函数（有返回值）
  Order order = lockService.lock("order:1001", 30, TimeUnit.SECONDS, () -> {
    // 在这里执行需要加锁的业务逻辑
    return orderService.getOrder("1001");
  });
  
  // 不等待的锁获取 + 回调函数（无返回值）
  lockService.lock("order:1001", 30, TimeUnit.SECONDS, () -> {
    // 在这里执行需要加锁的业务逻辑
    orderService.updateOrder("1001");
  });
```

### 方法说明

1. **tryLock** - 尝试获取锁，可以等待一段时间
   ```
   boolean tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit)
   ```
   - key: 锁的唯一标识
   - waitTime: 获取锁的最长等待时间
   - leaseTime: 锁的最长持有时间，超过这个时间锁会自动释放
   - timeUnit: 时间单位
   - 返回值: 获取锁成功返回true，失败返回false

2. **tryLock with callback** - 尝试获取锁并执行回调函数，自动处理锁的释放
   ```
   <T> T tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> callback)
   void tryLock(String key, long waitTime, long leaseTime, TimeUnit timeUnit, Runnable callback)
   ```
   - key: 锁的唯一标识
   - waitTime: 获取锁的最长等待时间
   - leaseTime: 锁的最长持有时间，超过这个时间锁会自动释放
   - timeUnit: 时间单位
   - callback: 获取锁成功后执行的回调函数
   - 返回值: 回调函数的返回值，如果获取锁失败则返回null

3. **lock** - 尝试获取锁，不等待（失败立即返回）
   ```
   boolean lock(String key, long leaseTime, TimeUnit timeUnit)
   ```
   - key: 锁的唯一标识
   - leaseTime: 锁的最长持有时间，超过这个时间锁会自动释放
   - timeUnit: 时间单位
   - 返回值: 获取锁成功返回true，失败返回false

4. **lock with callback** - 尝试获取锁并执行回调函数，自动处理锁的释放
   ```
   <T> T lock(String key, long leaseTime, TimeUnit timeUnit, Supplier<T> callback)
   void lock(String key, long leaseTime, TimeUnit timeUnit, Runnable callback)
   ```
   - key: 锁的唯一标识
   - leaseTime: 锁的最长持有时间，超过这个时间锁会自动释放
   - timeUnit: 时间单位
   - callback: 获取锁成功后执行的回调函数
   - 返回值: 回调函数的返回值，如果获取锁失败则返回null

5. **unlock** - 释放锁
   ```
   void unlock(String key)
   ```
   - key: 锁的唯一标识