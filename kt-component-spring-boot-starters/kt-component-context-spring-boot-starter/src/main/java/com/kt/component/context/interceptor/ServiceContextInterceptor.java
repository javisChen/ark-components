
package com.kt.component.context.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.cache.redis.RedisService;
import com.kt.component.common.id.TraceIdUtils;
import com.kt.component.context.LoginUserContext;
import com.kt.component.context.ServiceContext;
import com.kt.component.context.support.RedisKeyConst;
import com.kt.component.context.token.AccessTokenExtractor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 服务上下文拦截器
 * 每个服务被调用时都会经过当前拦截器，主要作用：
 * 1.业务校验
 * 2.设置通用的上下文参数（当前用户、当前TraceId等）
 * @author victor
 */
@Component
public class ServiceContextInterceptor implements HandlerInterceptor {

    private final RedisService redisService;
    private final AccessTokenExtractor accessTokenExtractor;
    private final String traceIdKey = "X-Trace-Id";

    public ServiceContextInterceptor(RedisService redisService,
                                     AccessTokenExtractor accessTokenExtractor) {
        this.redisService = redisService;
        this.accessTokenExtractor = accessTokenExtractor;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        setLoginUserContext(request);

        setTraceContext(request);
        return true;
    }

    private void setTraceContext(HttpServletRequest request) {
        String traceId = request.getHeader(traceIdKey);
        if (StringUtils.isEmpty(traceId)) {
            traceId = TraceIdUtils.getId();
        }
        ServiceContext.setContext(ServiceContext.TRACE_ID_KEY, traceId);
        MDC.put(traceIdKey, traceId);
    }

    private void setLoginUserContext(HttpServletRequest request) {
        String accessToken = accessTokenExtractor.extract(request);
        if (StringUtils.isNotEmpty(accessToken)) {
            Object cache = getUserCacheByToken(createAccessTokenKey(accessToken));
            if (Objects.nonNull(cache)) {
                LoginUserContext loginUserContext = JSONObject.parseObject((String) cache, LoginUserContext.class);
                ServiceContext.setContext(ServiceContext.LOGIN_USER_CONTEXT_KEY, loginUserContext);
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        ServiceContext.clearContext();
        MDC.remove(traceIdKey);
    }

    private Object getUserCacheByToken(String accessTokenKey) {
        return redisService.get(accessTokenKey);
    }

    private String createAccessTokenKey(String accessToken) {
        return RedisKeyConst.LOGIN_USER_ACCESS_TOKEN_KEY_PREFIX + accessToken;
    }
}
