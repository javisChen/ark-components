# ark-component-logging

## 作用
统一管理日志相关，目前只用作统一logback.xml，各个微服务只需要include该模板即可

## 模块介绍

| 模块                         | 作用  |     |
|----------------------------|-----|-----|
| ark-component-logging-core | 核心包 |     |

## 使用说明
在微服务的logback中include即可，可以自定义替换base里面的变量

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <!-- 引入公用配置 -->
    <include resource="com/ark/component/logging/ark-logback-base.xml"/>

</configuration>
```