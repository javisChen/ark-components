package com.kt.component.statemachine.core;

import com.kt.component.statemachine.core.action.ActionExecutor;
import com.kt.component.statemachine.core.exception.StateMachineException;
import com.kt.component.statemachine.core.guard.GuardExecutor;
import com.kt.component.statemachine.core.service.StateMachineService;
import com.kt.component.statemachine.dao.entity.StateMachineHistoryDO;
import com.kt.component.statemachine.dao.entity.StateMachineRuntimeDO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 状态机
 * @author jc
 */
@Slf4j
public class StateMachineExecutor {

    private final StateMachineService stateMachineService;
    private final GuardExecutor guardExecutor;
    private final ActionExecutor actionExecutor;
    private final StateMachineTransactionAction transactionAction;

    public StateMachineExecutor(StateMachineService stateMachineService,
                                GuardExecutor guardExecutor,
                                ActionExecutor actionExecutor,
                                StateMachineTransactionAction transactionAction) {
        this.stateMachineService = stateMachineService;
        this.guardExecutor = guardExecutor;
        this.actionExecutor = actionExecutor;
        this.transactionAction = transactionAction;
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
        return executeTransition(runtime, stateMachineDefinition, context);
    }

    private StateMachineResult executeTransition(StateMachineRuntimeDO runtime,
                                                 StateMachineDefinition definition,
                                                 StateMachineContext context) {
        List<StateMachineDefinition.Transitions> transitions = definition.getTransitions();
        String event = context.getEvent();
        if (Objects.isNull(runtime)) {
            // 如果runtime为空，检查一下驱动事件是否initEvent
            if (!definition.getInitEvent().equals(event)) {
                throw new StateMachineException("current event is not init event");
            }
            // 约定Transitions第一个对象是初始驱动事件配置，直接执行。
            return doExecuteTransition(context, transitions.get(0), definition, runtime);
        }

        // 判断业务状态是否已经结束
        if (runtime.getFinished()) {
            throw new StateMachineException("state already finished");
        }

        for (StateMachineDefinition.Transitions transition : transitions) {
            // 根据source和event确定需要执行的transition
            if (transition.getSource().equals(runtime.getState()) && transition.getEvent().equals(event)) {
                return doExecuteTransition(context, transition, definition, runtime);
            }
        }
        return StateMachineResult.fail(String.format("cannot find match transition for state:[%s] and event:[%s]", runtime, event));
    }

    private StateMachineResult doExecuteTransition(StateMachineContext context,
                                                   StateMachineDefinition.Transitions transition,
                                                   StateMachineDefinition definition,
                                                   StateMachineRuntimeDO runtimeDO) {
        List<String> guards = transition.getGuards();
        if (!guardExecutor.execute(guards, context)) {
            return StateMachineResult.fail("Couldn't get past the guards");
        }

        // 通过transactionAction来需要更新数据库的操作，尽量控制事务范围
        transactionAction.execute(() -> {

            actionExecutor.execute(transition.getActions(), context);

            String state = definition.getInitState();
            if (Objects.nonNull(runtimeDO)) {
                state = transition.getTarget();
            }
            saveStateMachineInfo(context, transition, definition, runtimeDO, state);

        });
        return StateMachineResult.success();
    }

    private void saveStateMachineInfo(StateMachineContext context,
                                      StateMachineDefinition.Transitions transition,
                                      StateMachineDefinition definition,
                                      StateMachineRuntimeDO runtimeDO,
                                      String state) {
        String bizCode = context.getBizCode();
        Long bizId = context.getBizId();
        String event = context.getEvent();
        Long id = runtimeDO != null ? runtimeDO.getId() : null;
        boolean finalEvent = isFinalEvent(transition, definition);
        stateMachineService.saveRuntime(id, bizCode, bizId, state, finalEvent);

        StateMachineHistoryDO stateMachineHistoryDO = new StateMachineHistoryDO();
        stateMachineHistoryDO.setBizCode(bizCode);
        stateMachineHistoryDO.setBizId(bizId);
        stateMachineHistoryDO.setEvent(event);
        stateMachineHistoryDO.setPreState(runtimeDO == null ? "" : runtimeDO.getState());
        stateMachineHistoryDO.setCurrentState(transition.getTarget());
        stateMachineService.saveHistory(stateMachineHistoryDO);
    }

    private boolean isFinalEvent(StateMachineDefinition.Transitions transition, StateMachineDefinition definition) {
        return definition.getFinalState().equals(transition.getTarget());
    }

    private StateMachineContext assembleContext(String bizCode, Long bizId, String event, Object params) {
        return new StateMachineContext(bizCode, bizId, event, params, new HashMap<>(16));
    }

    private StateMachineRuntimeDO getRuntime(StateMachineContext context) {
        return stateMachineService.getRuntime(context.getBizCode(), context.getBizId());
    }

    private void checkEventIsLegal(String event, StateMachineDefinition stateMachineDefinition) {
        if (!stateMachineDefinition.getEvents().contains(event)) {
            throw new StateMachineException("event is illegal");
        }
    }
}
