package com.ark.component.security.core.context.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.context.SecurityContextRepository;

@Slf4j
public abstract class AbstractSecurityContextRepository implements SecurityContextRepository {

    public AbstractSecurityContextRepository() {
    }

}
