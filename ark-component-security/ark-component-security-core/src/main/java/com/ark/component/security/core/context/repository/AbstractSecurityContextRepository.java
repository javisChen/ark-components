package com.ark.component.security.core.context.repository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.context.SecurityContextRepository;

@Slf4j
public abstract class AbstractSecurityContextRepository implements SecurityContextRepository {

    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();

    public AbstractSecurityContextRepository() {
    }

    protected String resolveToken(HttpServletRequest request) {
        return bearerTokenResolver.resolve(request);
    }

}
