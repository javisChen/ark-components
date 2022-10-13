package com.ark.biz.log.parse.support;

public class DefaultOperatorService implements IOperatorService {

    @Override
    public Operator get() {
        return new Operator(0L, "default");
    }
}
