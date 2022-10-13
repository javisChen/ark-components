package com.ark.component.statemachine.core;

import lombok.Data;

import java.util.Map;

/**
 * 状态机
 * @author jc
 */
@Data
public class StateMachineContext {

   private String bizCode;
   private Long bizId;
   private String event;
   private Object params;
   private Map<String, Object> extParams;

   public StateMachineContext(String bizCode, String event, Object params, Map<String, Object> extParams) {
      this.bizCode = bizCode;
      this.event = event;
      this.params = params;
      this.extParams = extParams;
   }

   public StateMachineContext(String bizCode, Long bizId, String event, Object params, Map<String, Object> extParams) {
      this.bizCode = bizCode;
      this.bizId = bizId;
      this.event = event;
      this.params = params;
      this.extParams = extParams;
   }
}
