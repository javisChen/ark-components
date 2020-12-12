package com.kt.demo;

import lombok.Data;

/**
 * 描述：随便定义一个Service
 *
 * @Author javis
 * @Date 2019/5/7 21:59
 * @Version V1.0
 **/
@Data
public class DemoService {

    private String sayWhat;
    private String toWho;

    public DemoService(String sayWhat, String toWho) {
        this.sayWhat = sayWhat;
        this.toWho = toWho;
    }

    public String say() {
        return this.sayWhat + "!  " + toWho;
    }
}