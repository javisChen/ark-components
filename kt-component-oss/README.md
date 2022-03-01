# kt-component-oss

## 作用
封装OSS相关处理，kt-component-oss-api模块是最顶层接口，如果需要接入新的OSS是只需要参考aliyun/minio模块进行实现即可。
starter为自动装配，引入即可直接使用。

## 模块介绍

| 模块  | 作用                |     |
|-----|-------------------|-----|
|kt-component-oss-api     | 定义OSS相关操作接口       |     |
|kt-component-oss-aliyun     | 阿里云OSS实现          |     |
|kt-component-oss-minio     | MinIo OSS实现       |     |
|kt-component-oss-aliyun-spring-boot-starter     | 阿里云OSS Starter    |     |
|kt-component-oss-minio-spring-boot-starter     | MinIo OSS Starter |     |

## 使用说明

阿里云
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





