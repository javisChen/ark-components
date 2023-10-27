package com.ark.component.statemachine.core.lock;

import com.ark.component.statemachine.core.StateData;
import org.apache.commons.collections4.keyvalue.MultiKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultStateMachineLock<S> implements StateMachineLock<S> {

    private final Map<MultiKey<String>, Object> lockedContext = new ConcurrentHashMap<>();

    @Override
    public boolean lock(StateData context) {
        Object o = new Object();
        MultiKey<String> multiKey = createKey(context);
        return lockedContext.putIfAbsent(multiKey, o) == null;
    }

    @Override
    public void unlock(StateData context) {
        lockedContext.remove(createKey(context));
    }

    private MultiKey<String> createKey(StateData context) {
        return new MultiKey<>(context.getMachineId(), context.getBizId());
    }

}
