package com.kt.component;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.kt.component.dao")
public class MybatisConfig {

    @Bean
    public IdentifierGenerator idGenerator() {
        return new DefaultIdentifierGenerator(1, 1);
    }
}
