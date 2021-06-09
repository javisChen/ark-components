package com.kt.component.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * @author JavisChen
 * @desc 响应常量管理
 * @date 2018/5/29 下午2:55
 */
@AllArgsConstructor
@Getter
public enum ResponseEnums {

    /**
     * 成功编码
     */
    OK("000000", "ok"),

    /**
     * A0001 ~ A1000是系统预留的用户端错误码
     */
    USER_ERROR("A0001", "用户端错误"),
    USER_METHOD_NOT_ALLOWED("A0002", "Request method not supported"),
    USER_METHOD_ARGUMENT_NOT_VALID("A0003", "Bad request：argument is invalid"),
    USER_REQUIRED_REQUEST_BODY_IS_MISSING("A0004", "Required request body is missing"),
    USER_NOT_LOGIN("A0005", "Authentication failed：token is required"),
    USER_LOGIN_FAIL("A0006", "Authentication failed：auth info is invalid"),
    USER_ACCESS_DENIED("A0007", "Authorization failed: access is denied"),
    USER_AUTHENTICATION_FAIL("A0008", "Authentication failed：token is invalid"),

    SERVER_ERROR("B0001", "Server error：unknown error"),
    THIRD_PARTY_ERROR("C00001", "Server error：third party has error");

    private final String code;
    private final String msg;

}
