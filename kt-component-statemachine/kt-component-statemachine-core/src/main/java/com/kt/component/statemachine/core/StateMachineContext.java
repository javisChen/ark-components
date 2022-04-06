package com.kt.component.statemachine.core;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 状态机shangxiawen
 * @author jc
 */
@Data
@Builder
public class StateMachineContext {

   private String bizCode;
   private Long bizId;
   private String event;
   private Map<String, Object> params;

}
