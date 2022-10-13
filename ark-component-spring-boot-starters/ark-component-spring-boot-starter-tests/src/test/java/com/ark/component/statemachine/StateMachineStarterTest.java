package com.ark.component.statemachine;

import com.ark.component.statemachine.core.StateMachineExecutor;
import com.ark.component.statemachine.core.StateMachineResult;
import com.ark.tests.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StateMachineStarterTest extends ApplicationTests {

    @Autowired
    private StateMachineExecutor stateMachineExecutor;

    @Test
    public void test() {
        try {
            StateMachineResult execute = stateMachineExecutor
                    .execute("order", 3L, "CREATE_ORDER", null);
            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
