package com.kt.component.dto.auth;

import lombok.Data;

import java.util.Set;

/**
 * 登录用户返回信息
 */
@Data
public class LoginUser {

    private String token;
    private Set<String> permissionCode;
}
