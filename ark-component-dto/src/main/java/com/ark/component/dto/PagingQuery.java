package com.ark.component.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Max;
import java.io.Serializable;

/**
 * 分页查询基类
 * 提供分页和排序相关的基础参数
 *
 * @author JC
 */
@Schema(description = "分页查询参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class PagingQuery extends BaseQuery {

    @Schema(description = "当前页码", 
            example = "1", 
            defaultValue = "1",
            title = "页码",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer current = 1;

    @Schema(description = "每页显示条数", 
            example = "10", 
            defaultValue = "10",
            title = "页大小",
            maximum = "5000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Max(value = 5000, message = "一次最多只能取5000条数据")
    private Integer size = 10;

    @Schema(description = "排序方向", 
            example = "desc", 
            title = "排序方向",
            allowableValues = {"asc", "desc"},
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortDirection;

    @Schema(description = "排序字段", 
            example = "createTime",
            title = "排序字段",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortField;

    @Schema(description = "降序排序字段列表", 
            example = "createTime,updateTime",
            title = "降序字段",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortDesc;

    @Schema(description = "升序排序字段列表", 
            example = "id,name",
            title = "升序字段",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String sortAsc;
}