package com.kt.component.statemachine.autoconfigure;

import com.kt.component.statemachine.core.StateMachineExecutor;
import com.kt.component.statemachine.core.StateMachineTransactionAction;
import com.kt.component.statemachine.core.action.ActionExecutor;
import com.kt.component.statemachine.core.guard.GuardExecutor;
import com.kt.component.statemachine.core.service.StateMachineService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

@Slf4j
@MapperScan("com.kt.component.statemachine.dao.mapper")
public class StateMachineAutoConfiguration {

    public StateMachineAutoConfiguration() {
        log.info("enable [kt-component-statemachine-spring-boot-starter]");
    }

    @Bean
    public StateMachineExecutor stateMachineExecutor(StateMachineService stateMachineService,
                                                     GuardExecutor guardExecutor,
                                                     ActionExecutor actionExecutor,
                                                     StateMachineTransactionAction transactionAction) {
        return new StateMachineExecutor(stateMachineService, guardExecutor, actionExecutor, transactionAction);
    }

}
