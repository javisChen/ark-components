package com.kt.component.statemachine.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态机执行结果
 * @author jc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "create", setterPrefix = "with")
public class StateMachineResult {

    private String bizCode;
    private Long bizId;
    private String state;
    private String event;
    private String msg;
    private Boolean success;

    public StateMachineResult(Boolean success) {
        this.success = success;
    }
    public StateMachineResult(String msg, Boolean success) {
        this.success = success;
        this.msg = msg;
    }

//    public static StateMachineResult success(StateMachineContext context) {
//        return new StateMachineResult(context.getBizCode(), context.getBizId(), "", "context.getEvent()", "success", true);
//    }
    public static StateMachineResultBuilder withSuccess() {
        return StateMachineResult.create().withSuccess(true).withMsg("success");
    }

//    public static StateMachineResult fail(StateMachineContext context, String msg) {
//        return new StateMachineResult(context.getBizCode(), context.getBizId(), context.getEvent(), msg, false);
//    }

    public static StateMachineResultBuilder withFail(String msg) {
        return StateMachineResult.create().withSuccess(false).withMsg(msg);
    }
}
