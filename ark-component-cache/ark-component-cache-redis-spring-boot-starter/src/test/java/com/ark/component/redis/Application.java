package com.ark.component.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.ark.component.redis.Application
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = {"com.ark.component.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
