package com.kt.component.statemachine.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态机执行结果
 * @author jc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateMachineResult {

    private String msg;
    private Boolean success;

    public static StateMachineResult success() {
        return new StateMachineResult("success", true);
    }

    public static StateMachineResult fail(String msg) {
        return new StateMachineResult(msg, false);
    }
}
