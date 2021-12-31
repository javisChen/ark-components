package com.kt.component.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.kt.component.dao")
public class MybatisConfig {

//    @Bean
//    public IdentifierGenerator idGenerator() {
//        return new DefaultIdentifierGenerator(1, 1);
//    }
}
