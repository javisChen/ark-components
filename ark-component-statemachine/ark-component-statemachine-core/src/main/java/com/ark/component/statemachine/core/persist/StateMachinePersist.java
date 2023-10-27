package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateData;

public interface StateMachinePersist {

	StateData read(String machineId, String bizId);

	void write(StateData stateData);
}
