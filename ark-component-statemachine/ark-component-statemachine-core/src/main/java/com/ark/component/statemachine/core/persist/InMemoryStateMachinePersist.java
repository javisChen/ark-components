package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateData;
import org.apache.commons.collections4.keyvalue.MultiKey;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStateMachinePersist<S> implements StateMachinePersist<S> {
    private final Map<MultiKey<String>, StateData<S>> repository = new HashMap<>(16);

    @Override
    public void write(StateData<S> context) {
        repository.put(new MultiKey<>(context.getMachineId(), context.getBizId()), context);
    }

    @Override
    public StateData<S> read(String machineId, String bizId) {
        return repository.get(new MultiKey<>(machineId, bizId));
    }
}
