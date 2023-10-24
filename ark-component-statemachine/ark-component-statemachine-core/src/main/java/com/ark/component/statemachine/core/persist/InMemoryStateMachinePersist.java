package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateData;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStateMachinePersist<S, E, T> implements StateMachinePersist<S, E, T> {

    private final Map<String, StateData<S>> repository = new HashMap<>(16);

    @Override
    public void write(StateData<S> context) {
        repository.put(context.getId(), context);
    }

    @Override
    public StateData<S> read(String id) {
        return repository.get(id);
    }
}
