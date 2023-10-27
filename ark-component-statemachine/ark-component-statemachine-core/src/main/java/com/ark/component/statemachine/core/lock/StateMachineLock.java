package com.ark.component.statemachine.core.lock;

import com.ark.component.statemachine.core.StateData;

public interface StateMachineLock<S> {

    boolean lock(StateData context);

    void unlock(StateData context);
}
