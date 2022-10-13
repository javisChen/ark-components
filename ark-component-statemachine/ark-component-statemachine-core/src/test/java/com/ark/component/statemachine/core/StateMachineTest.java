package com.ark.component.statemachine.core;

import com.ark.component.statemachine.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StateMachineTest extends ApplicationTests {

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