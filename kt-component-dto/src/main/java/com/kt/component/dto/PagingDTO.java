package com.kt.component.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 需要分页的请求体
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PagingDTO extends BaseDTO implements Serializable {

    private Integer current = 1;
    private Integer size = 10;
    private String sortDirection;
    private String sortField;
    private String sortDesc;
    private String sortAsc;
}
