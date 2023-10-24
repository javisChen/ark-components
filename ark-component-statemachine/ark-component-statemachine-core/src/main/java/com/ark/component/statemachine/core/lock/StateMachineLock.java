package com.ark.component.statemachine.core.lock;

import com.ark.component.statemachine.core.StateData;

public interface StateMachineLock<S> {

    boolean lock(StateData<S> context);

    void unlock(StateData<S> context);
}
