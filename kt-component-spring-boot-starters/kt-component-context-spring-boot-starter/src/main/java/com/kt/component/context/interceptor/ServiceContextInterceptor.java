
package com.kt.component.context.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.kt.component.cache.redis.RedisService;
import com.kt.component.context.LoginUserContext;
import com.kt.component.context.ServiceContext;
import com.kt.component.context.support.RedisKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ServiceContextInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getServletPath().equals("/v1/login/account")
        || request.getServletPath().equals("/v1/access/api")) {
            return true;
        }
        String accessToken = request.getHeader("Authorization").split("Bearer ")[1];
        Object cache = getCache(createAccessTokenKey(accessToken));
        LoginUserContext loginUserContext = JSONObject.parseObject((String) cache, LoginUserContext.class);
        ServiceContext.setLoginUserContext(loginUserContext);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        ServiceContext.clearContext();
    }

    private Object getCache(String accessTokenKey) {
        return redisService.get(accessTokenKey);
    }

    private String createAccessTokenKey(String accessToken) {
        return RedisKeyConst.LOGIN_USER_ACCESS_TOKEN_KEY_PREFIX + accessToken;
    }
}
