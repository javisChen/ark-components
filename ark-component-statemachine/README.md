# ark-component-statemachine

## 作用
状态机组件，可动态编排业务逻辑，把"状态"转换交给状态机，开发人员只需要关注业务本身。应用场景如：订单、工单等有大量状态转移的场景。

## 模块介绍

| 模块                             | 作用       |     |
|--------------------------------|----------|-----|
| ark-component-statemachine-core | 状态机核心逻辑  |     |
| ark-component-statemachine-dao  | 状态机持久化操作 |     |

## 使用说明

1.引入Maven依赖
```maven
<dependency>
    <groupId>com.ark.statemachine</groupId>
    <artifactId>ark-component-statemachine-spring-boot-starter</artifactId>
</dependency>
```

```java
try {
    StateMachineResult execute = stateMachineExecutor
            .execute("order", 3L, "CREATE_ORDER", null);
    System.out.println(execute);
} catch (Exception e) {
    e.printStackTrace();
}
```