package com.ark.component.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 响应类型为分页列表的响应体
 * @param <T>
 */
@Data
@Schema(description = "分页响应结果")
public class PageResponse<T> {

    @Schema(description = "总记录数",
            example = "100",
            title = "总记录数",
            defaultValue = "0")
    private Integer total = 0;

    @Schema(description = "每页显示数量",
            example = "10",
            title = "页大小",
            defaultValue = "1")
    private Integer size = 1;

    @Schema(description = "当前页码",
            example = "1",
            title = "当前页",
            defaultValue = "1")
    private Integer current = 1;

    @Schema(description = "分页数据列表",
            title = "数据列表")
    private List<T> records;

    public PageResponse(int current, int size, int total, List<T> records) {
        this.total = total;
        this.size = size;
        this.current = current;
        this.records = records;
    }

    public PageResponse() {
    }

    public static <T> PageResponse<T> of(long current, long size, long total, List<T> records) {
        return new PageResponse<>((int)current, (int)size, (int)total, records);
    }

    public static <T> PageResponse<T> of(IPage<T> page) {
        return new PageResponse<>((int)page.getCurrent(), (int)page.getSize(), (int)page.getTotal(), page.getRecords());
    }
    public static PageResponse<?> empty(IPage<?> page) {
        return new PageResponse<>((int)page.getCurrent(), (int)page.getSize(), (int)page.getTotal(), null);
    }

    public <R> PageResponse<R> convert(Function<? super T, ? extends R> mapper) {
        if (this.records == null) {
            return new PageResponse<R>(this.current, this.size, this.total, null);
        }
        List<R> collect = this.records.stream().map(mapper).collect(Collectors.toList());
        return new PageResponse<>(this.current, this.size, this.total, collect);
    }

}
