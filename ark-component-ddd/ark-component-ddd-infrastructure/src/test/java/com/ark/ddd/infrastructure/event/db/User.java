package com.ark.ddd.infrastructure.event.db;

import com.ark.ddd.domain.AggregateRoot;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class User extends AggregateRoot {

    private String username;

    public User(String username) {
        this.username = username;
    }
}
