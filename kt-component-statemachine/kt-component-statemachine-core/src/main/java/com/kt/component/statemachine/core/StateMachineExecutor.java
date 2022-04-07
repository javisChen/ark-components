package com.kt.component.statemachine.core;

import com.kt.component.statemachine.core.action.ActionExecutor;
import com.kt.component.statemachine.core.exception.StateMachineException;
import com.kt.component.statemachine.core.guard.GuardExecutor;
import com.kt.component.statemachine.core.service.StateMachineService;
import com.kt.component.statemachine.dao.entity.StateMachineRuntimeDO;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 状态机
 * @author jc
 */
public class StateMachineExecutor {

    private final StateMachineService stateMachineService;
    private final GuardExecutor guardExecutor;
    private final ActionExecutor actionExecutor;

    public StateMachineExecutor(StateMachineService stateMachineService,
                                GuardExecutor guardExecutor,
                                ActionExecutor actionExecutor) {
        this.stateMachineService = stateMachineService;
        this.guardExecutor = guardExecutor;
        this.actionExecutor = actionExecutor;
    }


    /**
     * 执行状态机
     * @param bizCode 业务编码
     * @param bizId   业务ID
     * @param params  参数
     * @return 成功=true，失败=false
     */
    public <P> StateMachineResult execute(String bizCode, Long bizId, String event, P params) {

        // 根据bizCode查出状态机定义规则
        StateMachineDefinition stateMachineDefinition = stateMachineService.getDefinition(bizCode);

        // 检查事件是否存在
        checkEventIsLegal(event, stateMachineDefinition);

        // 组装上下文
        StateMachineContext context = assembleContext(bizCode, bizId, event, params);

        // 根据bizCode和bizId查出对应状态机运行时数据
        StateMachineRuntimeDO runtime = getRuntime(context);

        // 执行状态转换
        return executeTransition(event, runtime, stateMachineDefinition, context);
    }

    private StateMachineResult executeTransition(String event,
                                                   StateMachineRuntimeDO runtime,
                                                   StateMachineDefinition definition,
                                                   StateMachineContext context) {
        List<StateMachineDefinition.Transitions> transitions = definition.getTransitions();
        if (Objects.isNull(runtime)) {
            return executeInitTransition(event, definition, context, transitions);
        }

        // 判断业务状态是否已经结束
        if (runtime.getFinished()) {
            throw new StateMachineException("state already finished");
        }

        for (StateMachineDefinition.Transitions transition : transitions) {
            // 根据source和event确定需要执行的transition
            if (transition.getSource().equals(runtime.getState()) && transition.getEvent().equals(event)) {
                return doExecuteTransition(context, transition);
            }
        }
        return StateMachineResult.fail(String.format("cannot find match transition for state:[%s] and event:[%s]", runtime, event));
    }

    private StateMachineResult executeInitTransition(String event, StateMachineDefinition definition, StateMachineContext context, List<StateMachineDefinition.Transitions> transitions) {
        // 如果runtime为空，检查一下驱动事件是否initEvent
        if (!definition.getInitEvent().equals(event)) {
            throw new StateMachineException("current event is not init event");
        }
        // 首次执行状态机，初始化一条记录
        stateMachineService.initRuntime(context.getBizCode(), context.getBizId(), definition.getInitState());
        // 约定Transitions第一个对象是初始驱动事件配置，直接执行。
        StateMachineDefinition.Transitions transition = transitions.get(0);
        return doExecuteTransition(context, transition);
    }

    private StateMachineResult doExecuteTransition(StateMachineContext context, StateMachineDefinition.Transitions transition) {
        List<String> guards = transition.getGuards();
        if (!guardExecutor.execute(guards, context)) {
            return StateMachineResult.fail("Couldn't get past the guards");
        }
        // 执行action会开启事务
        actionExecutor.execute(transition.getActions(), context);
        return StateMachineResult.success();
    }

    private StateMachineContext assembleContext(String bizCode, Long bizId, String event, Object params) {
        return new StateMachineContext(bizCode, bizId, event, params, new HashMap<>(16));
    }

    private StateMachineRuntimeDO getRuntime(StateMachineContext context) {
        return stateMachineService.getRuntime(context.getBizCode(), context.getBizId());
    }

    private void checkFinalState(StateMachineRuntimeDO runtime) {
        if (runtime != null && runtime.getFinished()) {
            throw new StateMachineException("state already finished");
        }
    }

    private void checkEventIsLegal(String event, StateMachineDefinition stateMachineDefinition) {
        if (!stateMachineDefinition.getEvents().contains(event)) {
            throw new StateMachineException("event is illegal");
        }
    }
}
