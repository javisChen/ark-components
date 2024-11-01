# ark-component-tree

## 作用
由于每个系统中多多少少都会存在需要使用层级结构的模块，例如菜单管理、多级分类、组织架构等。该模块的作用就是把层级结构的统一操作以及公共字段抽取出来，例如添加节点、移动节点、查询子节点等。让业务层无需再重复编写层级相关代码，只需要关注具体业务本身。

## 模块介绍

| 模块                                                 | 作用       |
|----------------------------------------------------|----------|
| [ark-component-tree-dto](ark-component-tree-dto)   | DTO定义    | 
| [ark-component-tree-core](ark-component-tree-core) | 层级结构逻辑实现 |
| [ark-component-tree-spring-boot-starter](ark-component-tree-spring-boot-starter) | 装配层|

## Maven

```xml
<dependency>
    <groupId>com.ark.tree</groupId>
    <artifactId>ark-component-tree-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 使用方式
```java



```