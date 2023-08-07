package com.ark.component.security.core.authentication.filter;

import com.ark.component.security.core.common.SecurityConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AccessCheckFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!accessFromGateway(request) || !accessFromInnerService(request)) {
            log.warn("服务从非法渠道被访问，请注意。{}", request);
            return ;
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
