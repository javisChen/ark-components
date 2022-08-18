package com.kt.component.statemachine.core.action;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.exception.ActionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 状态机执行链
 * @author jc
 */
@Component
@Slf4j
public class ActionExecutor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void execute(List<String> actions, StateMachineContext context) {
        for (String action : actions) {
            Object bean;
            try {
                bean = applicationContext.getBean(action);
            } catch (BeansException e) {
                throw new ActionException(e);
            }
            if (!ClassUtils.isAssignable(Action.class, bean.getClass())) {
                String format = String.format("Action class [%s] must be extend [%s]", action, Action.class.getName());
                throw new ActionException(format);
            }
            try {
                Action actionInstance = (Action) bean;
                actionInstance.execute(context);
            } catch (Exception e) {
                throw new ActionException(e);
            }
        }
    }
}
