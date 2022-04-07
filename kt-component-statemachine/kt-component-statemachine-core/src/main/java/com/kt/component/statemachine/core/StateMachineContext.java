package com.kt.component.statemachine.core;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * 状态机
 * @author jc
 */
@Data
@AllArgsConstructor
public class StateMachineContext {

   private String bizCode;
   private Long bizId;
   private String event;
   private Object params;
   private Map<String, Object> extParams;

}
