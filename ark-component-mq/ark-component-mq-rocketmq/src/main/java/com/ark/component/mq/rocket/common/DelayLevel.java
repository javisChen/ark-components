package com.ark.component.mq.rocket.common;

public enum DelayLevel {

    _1S(1, "1s"),
    _5S(2, "5s"),
    _10S(3, "10s"),
    _30S(4, "30s"),
    _1M(5, "1m"),
    _2M(6, "2m"),
    _3M(7, "3m"),
    _4M(8, "4m"),
    _5M(9, "5m"),
    _6M(10, "6m"),
    _7M(11, "7m"),
    _8M(12, "8m"),
    _9M(13, "9m"),
    _10M(14, "10m"),
    _20M(15, "20m"),
    _30M(16, "30m"),
    _1H(17, "1h"),
    _2H(18, "2h");

    private Integer value;
    private String text;

    DelayLevel(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
