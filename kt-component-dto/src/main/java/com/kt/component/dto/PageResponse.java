package com.kt.component.dto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

/**
 * 响应类型为分页列表的响应体
 * @param <T>
 */
@Data
public class PageResponse<T> {

    @ApiModelProperty(value = "总页数", required = false, notes = "")
    private Long total = 0L;
    @ApiModelProperty(value = "每页显示数量", required = false, notes = "")
    private Long size = 1L;
    @ApiModelProperty(value = "每页显示数量", required = false, notes = "")
    private Long current = 1L;
    @ApiModelProperty(value = "数据", required = false, notes = "")
    private Collection<T> records;

    public PageResponse(long current, long size,long total, Collection<T> records) {
        this.total = total;
        this.size = size;
        this.current = current;
        this.records = records;
    }

    public PageResponse() {
    }

    public static <T> PageResponse<T> build(long current, long size, long total, Collection<T> records) {
        return new PageResponse<>(current, size, total, records);
    }

    public static <T> PageResponse<T> build(IPage<T> page) {
        return new PageResponse<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

}
