package com.kt.catchlog.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 *
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = {"com.kt.catchlog.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
