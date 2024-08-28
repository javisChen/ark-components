package com.ark.component.security.core.authentication.filter;

import com.ark.component.common.id.TraceIdUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class TraceFilter extends OncePerRequestFilter {

    private static final String HEADER_TRACE_ID = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = request.getHeader(HEADER_TRACE_ID);
            if (StringUtils.isEmpty(traceId)) {
                traceId = TraceIdUtils.getId();
            }
            MDC.put(HEADER_TRACE_ID, traceId);
        } catch (Exception e) {
            log.error("Configure Trace Id Failure", e);
        }
        filterChain.doFilter(request, response);
    }

}
