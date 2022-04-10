package com.kt.component.statemachine.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@MapperScan("com.kt.component.statemachine.dao.mapper")
@ComponentScan(basePackages = "com.kt.component.statemachine.*")
public class StateMachineAutoConfiguration {

    public StateMachineAutoConfiguration() {
        log.info("enable [kt-component-statemachine-spring-boot-starter]");
    }

//    @Bean
//    public StateMachineExecutor stateMachineExecutor(StateMachineService stateMachineService,
//                                                     GuardExecutor guardExecutor,
//                                                     ActionExecutor actionExecutor,
//                                                     StateMachineTransactionAction transactionAction) {
//        return new StateMachineExecutor(stateMachineService, guardExecutor, actionExecutor, transactionAction);
//    }

}
