package com.kt.component.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import java.io.Serializable;

/**
 * 需要分页的请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PagingQuery extends BaseQuery implements Serializable {

    @ApiModelProperty(value = "当前页数", required = false, notes = "")
    private Integer current = 1;
    @ApiModelProperty(value = "每页显示数量", required = false, notes = "最多取5000条")
    @Max(value = 5000, message = "一次最多只能取5000条数据")
    private Integer size = 10;
    private String sortDirection;
    private String sortField;
    private String sortDesc;
    private String sortAsc;
}
