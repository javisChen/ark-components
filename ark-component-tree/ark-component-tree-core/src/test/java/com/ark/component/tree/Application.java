package com.ark.component.tree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication()
@MapperScan(basePackages = "com.ark.component.tree.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
