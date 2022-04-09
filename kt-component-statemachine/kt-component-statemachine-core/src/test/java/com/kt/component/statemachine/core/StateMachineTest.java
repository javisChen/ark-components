package com.kt.component.statemachine.core;

import com.kt.component.statemachine.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StateMachineTest extends ApplicationTests {

    @Autowired
    private StateMachineExecutor stateMachineExecutor;

    @Test
    public void test() {
        try {
            StateMachineResult execute = stateMachineExecutor
                    .execute("order", 1L, "CLOSE", null);
            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}