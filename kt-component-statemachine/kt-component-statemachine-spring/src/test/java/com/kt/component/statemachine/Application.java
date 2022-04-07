package com.kt.component.statemachine;

import com.kt.component.statemachine.config.Events;
import com.kt.component.statemachine.config.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

/**
 * Application
 *
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) {
        boolean b = stateMachine.sendEvent(Events.PAY);
        System.out.println(b);
    }
}
