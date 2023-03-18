## 介绍
整合常用主流`MQ`的组件，主旨在于一套标准的代码即可实现所有`MQ`的95%通用功能

## 设计

## 依赖
integration只负责整合所有MQ能力，不提供具体能力。按需引入MQ各自的starter。
```xml
<dependency>
    <groupId>com.ark.mq</groupId>
    <artifactId>ark-component-mq-integration-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- rocketmq -->
<dependency>
    <groupId>com.ark.mq</groupId>
    <artifactId>ark-component-mq-rocketmq-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- rabbitmq -->
<dependency>
    <groupId>com.ark.mq</groupId>
    <artifactId>ark-component-mq-rabbitmq-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 配置
```yaml
ark:
  component:
    mq:
      # rocketmq配置
      rocketmq:
        server: localhost:9876
        producer:
          group: 'pg_default'
      # rabbitmq配置        
      rabbitmq:
        server: localhost:5672
        # 交换机必须先提前在配置定义好        
        exchanges:
          - name: order
            type: fanout
```

## 使用

```java
@Autowired
private MessageTemplate messageTemplate;

@Test
public void testSendToRabbit(){
        MsgBody msgBody=buildBody();
        messageTemplate.send(MQType.RABBIT,MQTestConst.TOPIC_ORDER,MQTestConst.TAG_ORDER_CREATED,msgBody);
}

@Test
public void testSendToRocket(){
        MsgBody msgBody=buildBody();
        messageTemplate.send(MQType.ROCKET,MQTestConst.TOPIC_ORDER,MQTestConst.TAG_ORDER_CREATED,msgBody);
}

@Test
public void testSendToKafka(){
        MsgBody msgBody=buildBody();
        messageTemplate.send(MQType.KAFKA,MQTestConst.TOPIC_ORDER,MQTestConst.TAG_ORDER_CREATED,msgBody);
}
```