package com.kt.component.statemachine.core.guard;

import cn.hutool.core.util.ClassUtil;
import com.kt.component.statemachine.core.StateMachineContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 状态机执行守卫
 * @author jc
 */
@Slf4j
@Component
public final class GuardExecutor {

    private final ApplicationContext applicationContext;

    public GuardExecutor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public boolean execute(List<String> guards, StateMachineContext context) {
        for (String guard : guards) {
            Class<?> clazz;
            try {
                clazz = Class.forName(guard);
            } catch (ClassNotFoundException e) {
                log.error("Guard class [{}] not found", guard);
                return false;
            }
            if (!ClassUtil.isAssignable(Guard.class, clazz)) {
                log.error("Guard class [{}] must be extend [{}]", guard, Guard.class.getName());
                return false;
            }
            try {
                Guard guardInstance = ((Guard) applicationContext.getBean(clazz));
                if (!guardInstance.evaluate(context)) {
                    log.info("Guard [{}] no pass", guard);
                    return false;
                }
            } catch (Exception e) {
                log.error("Guard [" + guard + "] execute failure", e);
                return false;
            }
        }
        return true;
    }
}
