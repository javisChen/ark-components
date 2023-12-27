package com.ark.component.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = "com.ark.component.statemachine")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
