
package com.ark.component.context.core.interceptor;

import com.ark.component.common.id.TraceIdUtils;
import com.ark.component.context.core.ServiceContext;
import com.ark.component.security.core.authentication.AuthenticatedToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        // postHandle may not be called if an exception occurs
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            // 清理上下文，防止内存泄漏
            ServiceContext.clearContext();
            MDC.clear();
        } catch (Exception e) {
            // 确保清理异常不会影响请求处理
            // 但需要记录日志以便排查问题
            if (log.isWarnEnabled()) {
                log.warn("Error occurred while cleaning up context", e);
            }
        }
    }
}
