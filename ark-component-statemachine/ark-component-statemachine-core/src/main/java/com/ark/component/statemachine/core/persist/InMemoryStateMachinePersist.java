package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.StateData;
import org.apache.commons.collections4.keyvalue.MultiKey;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStateMachinePersist<S, E> implements StateMachinePersist<S, E> {
    private final Map<MultiKey<String>, StateData> repository = new HashMap<>(16);

    @Override
    public void write(StateData stateData, StateContext<S, E> context) {
        repository.put(new MultiKey<>(stateData.getMachineId(), stateData.getBizId()), stateData);
    }

    @Override
    public StateData read(String machineId, String bizId) {
        return repository.get(new MultiKey<>(machineId, bizId));
    }

}
