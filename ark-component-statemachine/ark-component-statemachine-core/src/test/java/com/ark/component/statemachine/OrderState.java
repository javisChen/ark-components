package com.ark.component.statemachine;

public enum OrderState {

    WAIT_PAY("待支付"),

    WAIT_DELIVER("待发货"),

    WAIT_RECEIVE("待收货"),

    WAIT_EVALUATE("待评价"),

    COMPLETED("已完成");

    private final String name;

    OrderState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
