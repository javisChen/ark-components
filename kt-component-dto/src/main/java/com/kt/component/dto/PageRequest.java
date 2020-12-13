package com.kt.component.dto;

import lombok.Data;

@Data
public class PageRequest<T> {

    private Integer current = 1;
    private Integer size = 10;
    private String sortDirection;
    private String sortField;
    private String sortDesc;
    private String sortAsc;
    private T params;
}
