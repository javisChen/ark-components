# ark-component-biz-log

## 作用

记录业务操作日志，基于Aop+表达式引擎（默认Spring EL）实现。

## 模块介绍

| 模块                             | 作用       |     |
|--------------------------------|----------|-----|
| ark-component-biz-log-context | 上下文  |     |
| ark-component-biz-log-core | 核心处理  |     |
| ark-component-biz-log-parse | 日志解析  |     |
| ark-component-biz-log-persistence | 持久化  |     |
| ark-component-biz-log-spring-boot-starter  | Spring Boot Stater |     |

## 使用说明

Maven

```xml

<dependency>
    <groupId>com.ark.component.biz.log</groupId>
    <artifactId>ark-component-biz-log-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

配置代码
```java
@Configuration
public class BeanConfig {

    @Resource
    private UserAclAdapter userAclAdapter;
    @Resource
    private WorkOrderFollowRecordService workOrderFollowRecordService;

    // 实现获取当前操作人方法
    @Bean
    public IOperatorService iOperatorService() {
        return () -> {
            UserInfoRespDto userInfo = userAclAdapter.getUserInfo(ServiceContext.getContext().getRequestUserId());
            return new Operator(userInfo.getUserId(), userInfo.getNickname());
        };
    }

    // 自定义持久化逻辑
    @Bean
    public BizLogRepository bizLogRepository() {
        return new BizLogRepository() {
            @Override
            public void save(BizLogInfo bizLogInfo) {
                workOrderFollowRecordService.save(null);
            }
        };
    }
}

```