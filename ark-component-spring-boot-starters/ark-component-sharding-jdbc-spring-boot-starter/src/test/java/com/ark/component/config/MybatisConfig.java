package com.ark.component.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ark.component.dao")
public class MybatisConfig {

//    @Bean
//    public IdentifierGenerator idGenerator() {
//        return new DefaultIdentifierGenerator(1, 1);
//    }
}
