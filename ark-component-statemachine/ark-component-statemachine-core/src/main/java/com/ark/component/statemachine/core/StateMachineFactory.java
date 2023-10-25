package com.ark.component.statemachine.core;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StateMachineFactory<S, E> {

    public Map<String, StateMachine<S, E>> machines = new HashMap<>(16);

    public StateMachine<S, E> acquireStateMachine(String machineId) {
        log.info("Acquiring machine with id " + machineId);
        StateMachine<S, E> stateMachine;
        // naive sync to handle concurrency with release
        stateMachine = machines.get(machineId);
        if (stateMachine == null) {
            log.info("Getting new machine from factory with id " + machineId);
            stateMachine = machines.get(machineId);
            machines.put(machineId, stateMachine);
        }
        return stateMachine;
    }

}
