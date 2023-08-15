package com.ark.component.cache.core.l1.caffeine;


import com.ark.component.cache.core.l1.L1Cache;
import org.springframework.cache.Cache;

public class CaffeineL1Cache extends L1Cache {

    public CaffeineL1Cache(Cache cache) {
        super(cache);
    }


}
