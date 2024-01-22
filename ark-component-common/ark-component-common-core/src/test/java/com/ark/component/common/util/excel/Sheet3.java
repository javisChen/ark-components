package com.ark.component.common.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Sheet3 {

    @ExcelProperty(index = 1)
    private String code;
    @ExcelProperty(index = 11)
    private String applications;
    @ExcelProperty(index = 12)
    private String menuPage;
}