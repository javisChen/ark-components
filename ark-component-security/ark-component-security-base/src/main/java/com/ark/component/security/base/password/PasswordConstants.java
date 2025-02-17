package com.ark.component.security.base.password;

/**
 * 密码相关常量
 *
 * @author JC
 */
public interface PasswordConstants {

    /**
     * 默认密码盐值
     * 用于加强密码安全性
     */
    String DEFAULT_PASSWORD_SALT = "6r4ogrp7th9rbyob";

    /**
     * 默认密码
     * 用于用户初始化或重置密码时使用
     */
    String DEFAULT_PASSWORD = "12345678";

    /**
     * 密码最小长度
     */
    int MIN_PASSWORD_LENGTH = 8;

    /**
     * 密码最大长度
     */
    int MAX_PASSWORD_LENGTH = 20;
} 