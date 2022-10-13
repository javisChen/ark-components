package com.ark.component.statemachine;

import com.ark.component.statemachine.core.StateMachineExecutor;
import com.ark.component.statemachine.core.action.ActionExecutor;
import com.ark.component.statemachine.core.guard.GuardExecutor;
import com.ark.component.statemachine.core.service.StateMachineService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Application
 * @author Javis
 * @date 2020-11-10 3:58 PM
 */
@SpringBootApplication(scanBasePackages = "com.ark.component.statemachine")
@MapperScan("com.ark.component.statemachine.dao.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public StateMachineExecutor stateMachineExecutor(StateMachineService stateMachineService,
                                                     GuardExecutor guardExecutor,
                                                     ActionExecutor actionExecutor) {
        return new StateMachineExecutor(stateMachineService, guardExecutor, actionExecutor);
    }
}
