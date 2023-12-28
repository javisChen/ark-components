package com.ark.component.web.util.separate;

import java.util.List;

public final class SeparateResult<T> {
    private final List<T> inserts;
    private final List<T> updates;

    public SeparateResult(List<T> inserts, List<T> updates) {
        this.inserts = inserts;
        this.updates = updates;
    }

    public List<T> getInserts() {
        return inserts;
    }

    public List<T> getUpdates() {
        return updates;
    }
}