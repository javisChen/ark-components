package com.ark.component.tree.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan(basePackages = {
        "com.ark.component.tree.dao",
})
public class TreeComponentMyBatisConfig {

}
