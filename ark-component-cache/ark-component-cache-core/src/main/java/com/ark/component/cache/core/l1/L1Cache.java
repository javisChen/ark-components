package com.ark.component.cache.core.l1;


import org.springframework.cache.Cache;

public abstract class L1Cache {

    private final Cache cache;

    public L1Cache(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }

}
