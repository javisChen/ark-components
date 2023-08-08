package com.ark.component.security.core.authentication.filter;

import com.ark.component.security.core.common.SecurityConst;
import com.ark.component.security.core.exception.IllegalRequestException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AccessCheckFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public AccessCheckFilter(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!accessFromGateway(request) && !accessFromInnerService(request)) {
            log.warn("服务从非法渠道被访问，请注意。{}", request);
            authenticationEntryPoint.commence(request, response, new IllegalRequestException("请通过合法渠道访问服务器！"));
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean accessFromInnerService(HttpServletRequest request) {
        String fromHeader = request.getHeader(SecurityConst.HEADER_FROM);
        return StringUtils.isNotBlank(fromHeader) && fromHeader.equals(SecurityConst.HEADER_FROM_SERVICE);
    }

    private boolean accessFromGateway(HttpServletRequest request) {
        String fromHeader = request.getHeader(SecurityConst.HEADER_FROM);
        return StringUtils.isNotBlank(fromHeader) && fromHeader.equals(SecurityConst.HEADER_FROM_GATEWAY);
    }
}
