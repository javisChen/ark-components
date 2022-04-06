package com.kt.component.statemachine.core.action;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.exception.StateMachineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 状态机执行链
 * @author jc
 */
@Component
@Slf4j
public class ActionExecutor {

    private final ApplicationContext applicationContext;

    public ActionExecutor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Transactional(rollbackFor = Exception.class)
    public void execute(List<String> actions, StateMachineContext context) {
        // 执行守卫
        for (String action : actions) {
            Class<?> clazz;
            try {
                clazz = Class.forName(action);
            } catch (ClassNotFoundException e) {
                throw new StateMachineException(String.format("Action class [%s] not found", action));
            }

            if (!clazz.isAssignableFrom(Action.class)) {
                String format = String.format("Guard class [%s] must be extend [com.kt.component.statemachine.core.action.Action]", action);
                throw new StateMachineException(format);
            }

            try {
                Action actionInstance = (Action) applicationContext.getBean(clazz);
                actionInstance.execute(context);
            } catch (Exception e) {
                throw new StateMachineException(String.format("Action [%s] execute failure", action), e);
            }
        }
    }
}
