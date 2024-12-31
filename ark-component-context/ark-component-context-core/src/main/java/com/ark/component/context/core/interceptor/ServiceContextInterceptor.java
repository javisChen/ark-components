
package com.ark.component.context.core.interceptor;

import com.ark.component.common.id.TraceIdUtils;
import com.ark.component.context.core.ServiceContext;
import com.ark.component.security.core.authentication.AuthenticatedToken;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.ark.component.context.core.contants.ContextConstants.*;

/**
 * 服务上下文拦截器
 * 每个服务被调用时都会经过当前拦截器，主要作用：
 * 1.业务校验
 * 2.设置通用的上下文参数（当前用户、当前TraceId等）
 * @author victor
 */
@RequiredArgsConstructor
public class ServiceContextInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        setLoginUserContext(request);

        setTraceContext(request);
        return true;
    }

    private void setTraceContext(HttpServletRequest request) {
        String traceId = request.getHeader(HEADER_TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = TraceIdUtils.getId();
        }
        ServiceContext.addContext(CONTEXT_KEY_TRACE_ID, traceId);
        MDC.put(HEADER_TRACE_ID, traceId);
    }

    private void setLoginUserContext(HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null || context.getAuthentication() == null) {
            return;
        }
        AuthenticatedToken authentication = (AuthenticatedToken) context.getAuthentication();
        ServiceContext.addContext(CONTEXT_KEY_LOGIN_USER, authentication.getAuthUser());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        ServiceContext.clearContext();
        MDC.remove(HEADER_TRACE_ID);
    }
}
