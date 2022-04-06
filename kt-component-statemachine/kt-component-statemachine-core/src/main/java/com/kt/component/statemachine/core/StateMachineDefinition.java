package com.kt.component.statemachine.core;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class StateMachineDefinition {

    @JSONField(name = "bizType")
    private String bizType;
    @JSONField(name = "states")
    private List<String> states;
    @JSONField(name = "events")
    private List<String> events;
    @JSONField(name = "initState")
    private String initState;
    @JSONField(name = "finalState")
    private String finalState;
    @JSONField(name = "transitions")
    private List<Transitions> transitions;

    @NoArgsConstructor
    @Data
    public static class Transitions {
        @JSONField(name = "guards")
        private List<String> guards;
        @JSONField(name = "source")
        private String source;
        @JSONField(name = "event")
        private String event;
        @JSONField(name = "target")
        private String target;
        @JSONField(name = "actions")
        private List<String> actions;
    }
}
