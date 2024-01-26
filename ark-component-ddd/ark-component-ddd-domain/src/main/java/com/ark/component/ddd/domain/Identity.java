package com.ark.component.ddd.domain;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

public interface Identity {
    static boolean isDuplicated(Collection<? extends Identity> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return false;
        }

        long count = collection.stream().map(Identity::getIdentifier).distinct().count();
        return count != collection.size();
    }

    Long getIdentifier();
}
