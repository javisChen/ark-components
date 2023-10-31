package com.ark.component.statemachine.core.persist;


import com.ark.component.statemachine.core.StateContext;
import com.ark.component.statemachine.core.StateData;

/**
 * 持久化状态数据接口
 *
 * @param <S>
 * @param <E>
 */
public interface StateMachinePersist<S, E> {

    /**
     * 读取状态数据
     *
     * @param machineId 状态机id
     * @param bizId     业务id
     */
    StateData read(String machineId, String bizId);

	/**
	 * 写入状态数据
	 * @param stateData
	 * @param stateContext
	 */
    void write(StateData stateData, StateContext<S, E> stateContext);
}
