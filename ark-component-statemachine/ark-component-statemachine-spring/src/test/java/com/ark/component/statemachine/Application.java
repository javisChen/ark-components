package com.ark.component.statemachine;

import com.ark.component.statemachine.config.Events;
import com.ark.component.statemachine.config.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;

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
    private StateMachineService<OrderStates, Events> stateMachineService;

    @Override
    public void run(String... args) {
        String machineId = "orderStateMachine";
        StateMachine<OrderStates, Events> acquireStateMachine = stateMachineService.acquireStateMachine(machineId, true);
        boolean b = acquireStateMachine.sendEvent(Events.RECEIVED);
    }
}
