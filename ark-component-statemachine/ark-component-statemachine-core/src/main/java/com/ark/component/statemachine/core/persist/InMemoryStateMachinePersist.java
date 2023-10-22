package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateMachineContext;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStateMachinePersist<S, E, T> implements StateMachinePersist<S, E, T> {

    private final Map<String, StateMachineContext<S, E, T>> repository = new HashMap<>(16);


    @Override
    public void write(StateMachineContext<S, E, T> context) throws Exception {
        repository.put(context.getId(), context);
    }

    @Override
    public StateMachineContext<S, E, T> read(String id) throws Exception {
        return repository.get(id);
    }
}
