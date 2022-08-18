package com.kt.component.statemachine.core.guard;

import com.kt.component.statemachine.core.StateMachineContext;
import com.kt.component.statemachine.core.exception.GuardException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * 状态机执行守卫
 * @author jc
 */
@Slf4j
@Component
public final class GuardExecutor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void execute(List<String> guards, StateMachineContext context) {
        for (String guard : guards) {
            Object bean;
            try {
                bean = applicationContext.getBean(guard);
            } catch (BeansException e) {
                throw new GuardException(e);
            }
            if (!ClassUtils.isAssignable(Guard.class, bean.getClass())) {
                String format = String.format("Guard class [%s] must be extend [%s]", guard, Guard.class.getName());
                log.error("找不到对应的bean");
                throw new GuardException(format);
            }
            try {
                Guard guardInstance = (Guard) bean;
                guardInstance.evaluate(context);
            } catch (Exception e) {
                throw new GuardException(e);
            }
        }
    }
}
