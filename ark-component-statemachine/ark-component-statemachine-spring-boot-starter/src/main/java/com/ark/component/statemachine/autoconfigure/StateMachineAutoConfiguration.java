package com.ark.component.statemachine.autoconfigure;

import com.ark.component.statemachine.core.StateMachineFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@Slf4j
//@MapperScan("com.ark.component.statemachine.dao.mapper")
//@ComponentScan(basePackages = "com.ark.component.statemachine.*")
public class StateMachineAutoConfiguration {

    public StateMachineAutoConfiguration() {
        log.info("enable [ark-component-statemachine-spring-boot-starter]");
    }

    @Bean
    @ConditionalOnMissingBean
    public StateMachineFactory stateMachineFactory() {
        return new StateMachineFactory();
    }

}
