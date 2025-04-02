# ARK Cache Component

## 简介
ARK Cache Component 提供统一的缓存操作接口，支持多种缓存实现（主要是Redis）。封装了字符串、Hash表、集合和计数器等常见缓存操作。

## 包名
- 接口定义: `com.ark.component.cache`

## 主要功能

### 字符串操作
```
  // 设置缓存
  cacheService.set("user:1001", userObject);
  // 设置带过期时间的缓存
  cacheService.set("user:1001", userObject, 3600L);
  cacheService.set("user:1001", userObject, 3600L, TimeUnit.SECONDS);
  // 获取缓存
  Object user = cacheService.get("user:1001");
  User user = cacheService.get("user:1001", User.class);
  // 当key不存在时设置值
  cacheService.setIfAbsent("user:1001", userObject);
  // 批量设置
  Map<String, Object> map = new HashMap<>();
  map.put("user:1001", user1);
  map.put("user:1002", user2);
  cacheService.mSet(map);
```

### Hash表操作
```
  // 批量设置Hash表
  Map<String, Object> userFields = new HashMap<>();
  userFields.put("name", "张三");
  userFields.put("age", 30);
  cacheService.hMSet("user:1001", userFields);
  cacheService.hMSet("user:1001", userFields, 3600L);
  // 获取Hash字段
  Object name = cacheService.hGet("user:1001", "name");
  // 批量获取Hash字段
  List<Object> values = cacheService.hMGet("user:1001", Arrays.asList("name", "age"));
  // 获取所有字段和值
  Map<Object, Object> allFields = cacheService.hGetAll("user:1001");
  // 获取所有值
  List<Object> allValues = cacheService.hVals("user:1001");
  // 增加字段值
  cacheService.hIncrBy("user:1001", "visits", 1L);
  // 删除字段
  cacheService.hDel("user:1001", Arrays.asList("visits", "lastLogin"));
```

### 集合操作
```
  // 添加集合元素
  cacheService.sAdd("roles:1001", "admin", "user");
  // 获取集合所有元素
  Set<Object> roles = cacheService.sMembers("roles:1001");
```

### 计数操作
```
  // 增加计数
  cacheService.incrBy("visits:page1", 1L);
  // 减少计数
  cacheService.decrBy("stock:item1", 1L);
```

### 删除操作
```
  // 删除单个缓存
  cacheService.del("user:1001");
  // 批量删除
  cacheService.del(Arrays.asList("user:1001", "user:1002"));
```

### 应用前缀支持
```
  // 使用应用前缀
  cacheService.set("myapp", "user:1001", userObject);
  Object user = cacheService.get("myapp", "user:1001");
  User user = cacheService.get("myapp", "user:1001", User.class);
  
  // Hash操作
  cacheService.hMSet("myapp", "user:1001", userFields);
  Object name = cacheService.hGet("myapp", "user:1001", "name");
``` 