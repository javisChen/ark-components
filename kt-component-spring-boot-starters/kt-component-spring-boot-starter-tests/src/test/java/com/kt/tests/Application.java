package com.kt.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = {"com.kt.component.*"})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
