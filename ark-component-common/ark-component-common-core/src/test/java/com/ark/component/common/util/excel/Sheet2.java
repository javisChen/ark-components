package com.ark.component.common.util.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Sheet2 {

    @ExcelProperty(index = 0)
    private String groupCode;
    @ExcelProperty(index = 1)
    private String code;
    @ExcelProperty(index = 10)
    private String applications;
    @ExcelProperty(index = 11)
    private String menuPage;
}