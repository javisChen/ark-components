package com.ark.component.statemachine.core.builder;

import com.ark.component.statemachine.core.lock.StateMachineLock;
import com.ark.component.statemachine.core.persist.StateMachinePersist;

public class StateMachineConfigurationBuilder<S, E> {

    private StateMachinePersist<S, E> persist;
    private StateMachineLock<S> lock;
    private String machineId;

    public StateMachineConfigurationBuilder<S, E> persist(StateMachinePersist<S, E> persist) {
        this.persist = persist;
        return this;
    }

    public StateMachineConfigurationBuilder<S, E> lock(StateMachineLock<S> lock) {
        this.lock = lock;
        return this;
    }

    public StateMachineConfigurationBuilder<S, E> machineId(String id) {
        this.machineId = id;
        return this;
    }

    public StateMachinePersist<S, E> getPersist() {
        return persist;
    }

    public StateMachineLock<S> getLock() {
        return lock;
    }

    public String getMachineId() {
        return machineId;
    }

}
