package com.ark.component.security.base.password;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认密码服务实现
 * 使用多重加密策略保护密码安全
 *
 * @author JC
 */
@Slf4j
public class DefaultPasswordService implements PasswordService {

    private final String passwordSalt;

    public DefaultPasswordService(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    /**
     * 加强密码安全性
     * 加密策略：bcrypt(password + salt)
     * 注意：password已经过前端双重MD5加密：md5(md5(rawPassword))
     */
    @Override
    public String enhancePassword(String password) {
        return DigestUtil.bcrypt(password + passwordSalt);
    }

    /**
     * 校验密码
     * 注意：password已经过前端双重MD5加密：md5(md5(rawPassword))
     */
    @Override
    public boolean checkPassword(String password, String passwordHashed) {
        return DigestUtil.bcryptCheck(password + passwordSalt, passwordHashed);
    }

    /**
     * 用于测试密码加密和验证流程
     */
    public static void main(String[] args) {
        // 模拟密码加密和验证流程
        String rawPassword = "88888888";
        
        // 1. 第一次MD5（模拟前端）
        String firstMd5 = DigestUtil.md5Hex(rawPassword);
        System.out.println("First MD5: " + firstMd5);
        
        // 2. 第二次MD5（模拟前端）
        String secondMd5 = DigestUtil.md5Hex(firstMd5);
        System.out.println("Second MD5: " + secondMd5);
        
        // 3. 后端加密（加盐 + bcrypt）
        DefaultPasswordService passwordService = new DefaultPasswordService("ark-salt");
        String hashedPassword = passwordService.enhancePassword(secondMd5);
        System.out.println("Hashed Password: " + hashedPassword);
        
        // 4. 验证密码
        // 模拟前端传入的密码（经过双重MD5）
        String inputPassword = DigestUtil.md5Hex(DigestUtil.md5Hex(rawPassword));
        boolean isValid = passwordService.checkPassword(inputPassword, hashedPassword);
        System.out.println("Password Valid: " + isValid);
    }
} 