package com.ark.component.security.core.token.generator;

import cn.hutool.core.util.RandomUtil;
import com.ark.component.security.base.user.LoginUser;
import com.ark.component.security.core.token.TokenMetadata;
import org.springframework.stereotype.Component;

/**
 * 简单Token生成器
 * 使用随机字符串作为token，支持自定义token长度
 *
 * @author JMox
 */
@Component
public class SimpleTokenGenerator implements TokenGenerator {
    
    private static final int TOKEN_LENGTH = 32;
    private static final int REFRESH_TOKEN_LENGTH = 64;
    
    @Override
    public String generateToken(TokenMetadata metadata, LoginUser loginUser) {
        // 生成指定长度的随机字符串，包含数字和字母
        return RandomUtil.randomString(TOKEN_LENGTH);
    }
    
    @Override
    public String generateRefreshToken() {
        return RandomUtil.randomString(REFRESH_TOKEN_LENGTH);
    }
} 