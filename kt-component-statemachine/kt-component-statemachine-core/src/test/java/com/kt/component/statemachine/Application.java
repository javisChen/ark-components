package com.kt.component.statemachine;

import com.kt.component.statemachine.core.StateMachineExecutor;
import com.kt.component.statemachine.core.action.ActionExecutor;
import com.kt.component.statemachine.core.guard.GuardExecutor;
import com.kt.component.statemachine.core.service.StateMachineService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Application
 *
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication
@MapperScan("com.kt.component.statemachine.dao.mapper")
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
    }

    @Bean
    public StateMachineExecutor stateMachineExecutor(StateMachineService stateMachineService,
                                                     GuardExecutor guardExecutor,
                                                     ActionExecutor actionExecutor) {
        return new StateMachineExecutor(stateMachineService, guardExecutor, actionExecutor);
    }
}
