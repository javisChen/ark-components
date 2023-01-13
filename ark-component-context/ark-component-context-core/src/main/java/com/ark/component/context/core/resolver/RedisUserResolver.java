package com.ark.component.context.core.resolver;

import com.alibaba.fastjson.JSONObject;
import com.ark.component.cache.redis.RedisCacheService;
import com.ark.component.context.core.LoginUserContext;
import com.ark.component.context.core.ServiceContext;
import com.ark.component.context.core.contants.RedisKeyConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.ark.component.context.core.contants.ContextConstants.LOGIN_USER_CONTEXT_KEY;

/**
 * 从Redis查出用户信息
 */
@RequiredArgsConstructor
public class RedisUserResolver implements UserResolver {

    private final RedisCacheService redisCacheService;

    @Override
    public LoginUserContext resolve(String token) {
        if (StringUtils.isNotEmpty(token)) {
            Object cache = redisCacheService.get(buildRedisKey(token));
            if (Objects.nonNull(cache)) {
                return JSONObject.parseObject((String) cache, LoginUserContext.class);
            }
        }
        return null;
    }

    private String buildRedisKey(String accessToken) {
        return RedisKeyConstants.LOGIN_USER_ACCESS_TOKEN_KEY_PREFIX + accessToken;
    }
}
