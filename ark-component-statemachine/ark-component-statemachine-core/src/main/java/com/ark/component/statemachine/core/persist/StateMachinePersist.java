package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateData;

public interface StateMachinePersist<S> {

	void write(StateData<S> context);

	StateData<S> read(String id);

}
