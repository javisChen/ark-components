package com.ark.component.statemachine.config;

import com.ark.component.statemachine.OrderStateMachineListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, Events> {

    @Autowired
    private OrderStateMachineListener orderStateMachineListener;

    @Bean
    public StateMachineService<OrderStates, Events> activityStateMachineService(StateMachineFactory<OrderStates, Events> stateMachineFactory) {
        return new DefaultStateMachineService<>(stateMachineFactory);
    }

//    @Bean
//    public StateMachineRuntimePersister<OrderStates, Events, Order> stateMachineRuntimePersister() {
//        RedisRepositoryStateMachinePersist<OrderStates, Events> persist = new RedisRepositoryStateMachinePersist<OrderStates, Events>();
//        return new RedisPersistingStateMachineInterceptor<>(persist);
//    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, Events> config)
            throws Exception {
        config
//                .withPersistence()
//                    .runtimePersister(new RedisPersistingStateMachineInterceptor<>())
//                .and()
                .withConfiguration()
                    .autoStartup(true)
                    .listener(orderStateMachineListener);
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, Events> states)
            throws Exception {
        states.withStates()
                .initial(OrderStates.WAIT_PAY, null)
                .state(OrderStates.PAID)
                .end(OrderStates.PAID)
                .states(EnumSet.allOf(OrderStates.class))
        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, Events> transitions)
            throws Exception {
        transitions
                .withExternal()
                .name("test1")
                .source(OrderStates.WAIT_PAY)
                .event(Events.CLOSE_ORDER)
                .target(OrderStates.PAID)
                .and()
                .withExternal()
                .name("test2")
                .source(OrderStates.PAID)
                .event(Events.PAY)
                .target(OrderStates.PAID)
                .and()
        ;
        // @formatter:off
    }

}