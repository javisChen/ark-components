package com.ark.ddd.infrastructure.event.db;

import lombok.Data;

@Data
public class Person {

    private String username;

    public Person(String username) {
        this.username = username;
    }
}
