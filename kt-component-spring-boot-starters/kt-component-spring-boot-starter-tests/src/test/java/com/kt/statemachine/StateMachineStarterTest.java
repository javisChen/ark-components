package com.kt.statemachine;

import com.kt.component.statemachine.core.StateMachineExecutor;
import com.kt.component.statemachine.core.StateMachineResult;
import com.kt.tests.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StateMachineStarterTest extends ApplicationTests {

    @Autowired
    private StateMachineExecutor stateMachineExecutor;

    @Test
    public void test() {
        try {
            StateMachineResult execute = stateMachineExecutor
                    .execute("order", 2L, "CREATE_ORDER", null);
            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
