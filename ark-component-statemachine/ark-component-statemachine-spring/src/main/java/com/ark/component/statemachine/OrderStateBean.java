package com.ark.component.statemachine;

import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@WithStateMachine(id = "orderStateMachine")
public class OrderStateBean {

    @OnTransition(source = "WAIT_PAY", target = "PAID")
    public void fromS1ToS2() {
    }

}
