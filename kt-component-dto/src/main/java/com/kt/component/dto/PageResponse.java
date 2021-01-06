package com.kt.component.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.Collection;

@Data
public class PageResponse<T> {

    private static final long serialVersionUID = 1L;

    private Long total = 0L;

    private Long size = 1L;

    private Long current = 1L;

    private Collection<T> records;


    public PageResponse(long current, long size,long total, Collection<T> records) {
        this.total = total;
        this.size = size;
        this.current = current;
        this.records = records;
    }

    public PageResponse() {
    }

    public static <T> PageResponse<T> success(IPage<T> page) {
        return new PageResponse<T>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    public static <T> PageResponse<T> success(long current, long size,long total, Collection<T> records) {
        return new PageResponse<T>(current, size, total, records);
    }

    public static PageResponse failure() {
        return new PageResponse<>();
    }
}
