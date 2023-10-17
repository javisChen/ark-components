package com.ark.component.statemachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, Events> {


    @Bean
    public StateMachineService<OrderStates, Events> activityStateMachineService(StateMachineFactory<OrderStates, Events> stateMachineFactory) {

        return new DefaultStateMachineService<>(stateMachineFactory, new StateMachinePersist<>() {

            private Map<String, StateMachineContext<OrderStates, Events>> orderMap = new ConcurrentHashMap<>();

            @Override
            public void write(StateMachineContext<OrderStates, Events> context, String contextObj) throws Exception {
                orderMap.put(contextObj, context);
            }

            @Override
            public StateMachineContext<OrderStates, Events> read(String contextObj) throws Exception {
                return orderMap.get(contextObj);
            }
        });
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStates, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, Events> states)
            throws Exception {
        states.withStates()
                .initial(OrderStates.WAIT_PAY)
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
                .target(OrderStates.PAID)
                .event(Events.PAY)
                .and()
        ;
        // @formatter:off
    }

    @Bean
    public StateMachineListener<OrderStates, Events> listener() {
        return new StateMachineListenerAdapter<OrderStates, Events>() {
            @Override
            public void stateChanged(State<OrderStates, Events> from, State<OrderStates, Events> to) {
                System.out.println("from = " + from);
                System.out.println(" State change to " + to.getId());
            }

            @Override public void eventNotAccepted(Message<Events> event) {
                super.eventNotAccepted(event);
    }};
    }
}