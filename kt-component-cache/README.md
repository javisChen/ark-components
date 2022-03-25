# kt-component-cache

## 作用
封装缓存相关处理，kt-component-cache-api模块是最顶层接口

## 模块介绍

| 模块                                           | 作用             |     |
|----------------------------------------------|----------------|-----|
| kt-component-oss-api                         | 定义Cache相关操作接口  |     |
| kt-component-cache-redis                     | Redis Cache 实现 |     |
| kt-component-cache-redis-spring-boot-starter | Redis Starter  |     |

## 使用说明

Redis
```java
AliYunOssProperties minIoConfiguration = new AliYunOssProperties();
minIoConfiguration.setEndPoint("");
minIoConfiguration.setAccessKey("");
minIoConfiguration.setSecretKey("");
// 初始化实例
IObjectStorageService iObjectStorageService; = new MinIoOssObjectStorageService(minIoOssProperties);

// 上传
InputStream resourceAsStream = getClass().getResourceAsStream("/test.txt");
String ossUrl = iObjectStorageService.put("code", "test.txt", resourceAsStream);

```

MinIo
```java
MinIoOssProperties minIoOssProperties = new MinIoOssProperties();
minIoOssProperties.setEndPoint("");
minIoOssProperties.setAccessKey("");
minIoOssProperties.setSecretKey("");
// 初始化实例
IObjectStorageService iObjectStorageService; = new MinIoOssObjectStorageService(minIoOssProperties);

// 上传
InputStream resourceAsStream = getClass().getResourceAsStream("/test.txt");
String ossUrl = iObjectStorageService.put("code", "test.txt", resourceAsStream);

```
Starter
```yaml
kt:
  component:
    oss:
      minio:
        end-point: http://127.0.0.1:9000
        access-key: admin
        secret-key: admin123456
        enabled: true
      aliyun:
        enabled: false
```





