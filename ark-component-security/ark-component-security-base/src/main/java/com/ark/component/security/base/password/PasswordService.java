package com.ark.component.security.base.password;

/**
 * 密码服务接口
 * 提供密码加密和验证功能
 *
 * @author JC
 */
public interface PasswordService {

    /**
     * 加强密码安全性
     * 使用多重加密策略：bcrypt(前端md5(md5(password)) + salt)
     *
     * @param password 原始密码（已经过前端双重MD5加密）
     * @return 加密后的密码
     */
    String enhancePassword(String password);

    /**
     * 校验密码
     * 验证输入的密码（已经过前端双重MD5加密）是否与存储的密码匹配
     *
     * @param password 输入的密码（已经过前端双重MD5加密）
     * @param passwordHashed 存储的加密密码
     * @return 是否匹配
     */
    boolean checkPassword(String password, String passwordHashed);
} 