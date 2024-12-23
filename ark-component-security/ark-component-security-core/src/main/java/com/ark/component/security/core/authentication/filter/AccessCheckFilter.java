package com.ark.component.security.core.authentication.filter;

import com.ark.component.security.core.common.SecurityConstants;
import com.ark.component.security.core.exception.IllegalRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AccessCheckFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final Environment environment;
    
    private static final String PROD_PROFILE = "prod";

    public AccessCheckFilter(ApplicationContext context, AuthenticationEntryPoint authenticationEntryPoint) {
        environment = context.getEnvironment();
        this.authenticationEntryPoint = authenticationEntryPoint;
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        if (shouldCheckAccess() && !isValidAccess(request)) {
            handleIllegalAccess(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
    
    /**
     * 判断是否需要检查访问来源
     */
    private boolean shouldCheckAccess() {
        // return Arrays.asList(environment.getActiveProfiles()).contains(PROD_PROFILE);
        return false;
    }
    
    /**
     * 检查是否是合法访问
     */
    private boolean isValidAccess(HttpServletRequest request) {
        String fromHeader = request.getHeader(SecurityConstants.HTTP_HEADER_SOURCE);
        return StringUtils.isNotBlank(fromHeader) && 
               (SecurityConstants.ACCESS_SOURCE_GATEWAY.equals(fromHeader) ||
                SecurityConstants.ACCESS_SOURCE_SERVICE.equals(fromHeader));
    }
    
    /**
     * 处理非法访问
     */
    private void handleIllegalAccess(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        log.warn("检测到非法访问请求: {}", request.getRequestURI());
        authenticationEntryPoint.commence(
            request, 
            response, 
            new IllegalRequestException("请通过合法渠道访问服务！")
        );
    }
}
