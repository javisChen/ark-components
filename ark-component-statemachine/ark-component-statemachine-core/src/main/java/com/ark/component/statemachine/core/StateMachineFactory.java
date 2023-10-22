package com.ark.component.statemachine.core;

import com.ark.component.cola.statemachine.impl.StateMachineException;
import com.ark.component.statemachine.core.persist.InMemoryStateMachinePersist;
import com.ark.component.statemachine.core.persist.StateMachinePersist;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StateMachineFactory {

    public Map<String, StateMachine<?, ?>> machines = new HashMap<>(16);

    private StateMachinePersist stateMachinePersist = new InMemoryStateMachinePersist<>();

    public <S, E> StateMachine<S, E> acquireStateMachine(String machineId, boolean start) {
        log.info("Acquiring machine with id " + machineId);
        StateMachine<?, ?> stateMachine;
        // naive sync to handle concurrency with release
        stateMachine = machines.get(machineId);
        if (stateMachine == null) {
            log.info("Getting new machine from factory with id " + machineId);
            stateMachine = machines.get(machineId);
            if (stateMachinePersist != null) {
                try {
                    StateMachineContext<S, E> stateMachineContext = stateMachinePersist.read(machineId);
                    stateMachine = restoreStateMachine(stateMachine, stateMachineContext);
                } catch (Exception e) {
                    log.error("Error handling context", e);
                    throw new StateMachineException("Unable to read context from store", e);
                }
            }
            machines.put(machineId, stateMachine);
        }
        return stateMachine;
    }
