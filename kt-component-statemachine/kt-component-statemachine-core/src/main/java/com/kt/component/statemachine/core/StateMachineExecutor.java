package com.kt.component.statemachine.core;

import com.kt.component.statemachine.core.action.ActionExecutor;
import com.kt.component.statemachine.core.exception.StateMachineException;
import com.kt.component.statemachine.core.guard.GuardExecutor;
import com.kt.component.statemachine.core.service.StateMachineService;
import com.kt.component.statemachine.dao.entity.StateMachineRuntimeDO;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 状态机
 * @author jc
 */
public class StateMachineExecutor {

    private StateMachineService stateMachineService;
    private GuardExecutor guardExecutor;
    private ActionExecutor actionExecutor;

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
    public StateMachineResult execute(String bizCode, Long bizId, String event, Map<String, Object> params) {

        // 根据bizCode查出状态机定义规则
        StateMachineDefinition stateMachineDefinition = stateMachineService.getDefinition(bizCode);

        // 检查事件是否存在
        checkEventIsLegal(event, stateMachineDefinition);

        // 根据bizCode和bizId查出对应状态机运行时数据
        StateMachineRuntimeDO runtime = getRuntime(bizCode, bizId, stateMachineDefinition);
        
        // 判断业务状态是否已经结束
        checkFinalState(runtime);

        // 组装上下文
        StateMachineContext context = assembleContext(bizCode, bizId, event, params);

        // 执行状态转换
        return executeTransition(event, runtime.getState(), stateMachineDefinition.getTransitions(), context);
    }

    private StateMachineResult executeTransition(String event,
                                                 String currentState,
                                                 List<StateMachineDefinition.Transitions> transitions,
                                                 StateMachineContext context) {
        for (StateMachineDefinition.Transitions transition : transitions) {
            // 根据source和event确定需要执行的transition
            if (transition.getSource().equals(currentState) && transition.getEvent().equals(event)) {
                List<String> guards = transition.getGuards();
                if (!guardExecutor.execute(guards, context)) {
                    return StateMachineResult.fail("Couldn't get past the guards");
                }
                // 执行action会开启事务
                actionExecutor.execute(transition.getActions(), context);
                return StateMachineResult.success();
            }
        }
        return StateMachineResult.fail(String.format("cannot find match transition for state:[%s] and event:[%s]", currentState, event));
    }

    private StateMachineContext assembleContext(String bizCode, Long bizId, String event, Map<String, Object> params) {
        return StateMachineContext.builder()
                .bizCode(bizCode)
                .bizId(bizId)
                .event(event)
                .params(params)
                .build();
    }

    private StateMachineRuntimeDO getRuntime(String bizCode, Long bizId, StateMachineDefinition stateMachineDefinition) {
        StateMachineRuntimeDO runtime = stateMachineService.getRuntime(bizCode, bizId);
        if (Objects.isNull(runtime)) {
            runtime = stateMachineService.initRuntime(bizCode, bizId, stateMachineDefinition.getInitState());
        }
        return runtime;
    }

    private void checkFinalState(StateMachineRuntimeDO runtime) {
        if (runtime.getFinished()) {
            throw new StateMachineException("state already finished");
        }
    }

    private void checkEventIsLegal(String event, StateMachineDefinition stateMachineDefinition) {
        if (!stateMachineDefinition.getEvents().contains(event)) {
            throw new StateMachineException("event is illegal");
        }
    }
}
