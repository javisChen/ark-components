package com.ark.component.statemachine.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StateMachineFactory implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @SuppressWarnings("rawtypes")
    private static final Map<String, StateMachineImpl> machines = new HashMap<>(16);

    @SuppressWarnings("unchecked")
    public <S, E> StateMachineImpl<S, E> get(String machineId) {
        return machines.get(machineId);
    }

    public <S, E> void register(String machineId, StateMachineImpl<S, E> stateMachineImpl) {
        if (machines.containsKey(machineId)) {
            throw new StateMachineException("{} machineId has been registered");
        }
        machines.put(machineId, stateMachineImpl);
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, StateMachineImpl> beans = applicationContext.getBeansOfType(StateMachineImpl.class);
        beans.forEach((beanName, machine) -> this.register(machine.getMachineId(), machine));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
