package com.kt.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Application
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = {"com.kt.component.*"})
@EnableScheduling
public class Application {

    @Autowired
    private TT tt;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void accountTask3() {
        tt.aaa();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
