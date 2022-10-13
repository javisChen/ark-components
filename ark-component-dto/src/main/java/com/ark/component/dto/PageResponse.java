package com.ark.component.dto;
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
    private Integer total = 0;
    @ApiModelProperty(value = "每页显示数量", required = false, notes = "")
    private Integer size = 1;
    @ApiModelProperty(value = "每页显示数量", required = false, notes = "")
    private Integer current = 1;
    @ApiModelProperty(value = "数据", required = false, notes = "")
    private Collection<T> records;

    public PageResponse(int current, int size, int total, Collection<T> records) {
        this.total = total;
        this.size = size;
        this.current = current;
        this.records = records;
    }

    public PageResponse() {
    }

    public static <T> PageResponse<T> of(long current, long size, long total, Collection<T> records) {
        return new PageResponse<>((int)current, (int)size, (int)total, records);
    }

    public static <T> PageResponse<T> of(IPage<T> page) {
        return new PageResponse<>((int)page.getCurrent(), (int)page.getSize(), (int)page.getTotal(), page.getRecords());
    }
    public static PageResponse<?> empty(IPage<?> page) {
        return new PageResponse<>((int)page.getCurrent(), (int)page.getSize(), (int)page.getTotal(), null);
    }

}
