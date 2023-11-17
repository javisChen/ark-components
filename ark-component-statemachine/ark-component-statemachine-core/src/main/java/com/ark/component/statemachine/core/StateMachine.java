package com.ark.component.statemachine.core;

import java.util.Collection;

/**
 * 状态机接口
 * @param <S> 状态
 * @param <E> 事件
 */
public interface StateMachine<S, E> {

    /**
     * 获取状态机id
     * @return 返回状态机id
     */
    String getMachineId();

    /**
     * 获取状态机所有事件
     * @return 返回状态机所有事件
     */
    Collection<Event<E>> getEvents();

    /**
     * 获取状态机所有状态
     * @return 返回状态机所有状态
     */
    Collection<State<S>> getStates();

    /**
     * 获取状态机初始状态
     * @return 返回状态机初始状态
     */
    State<S> getInitial();

    /**
     * 状态机状态转移
     * @param source 状态
     * @param event 事件
     * @param params 事件参数
     * @return 返回状态
     */
    <P> S nextState(S source, E event, P params);
}
