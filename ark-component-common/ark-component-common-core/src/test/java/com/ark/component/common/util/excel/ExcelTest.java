package com.ark.component.common.util.excel;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExcelTest {

    @Test
    public void test() {
        String fileName = "/Users/chenjiawei/code/myself/ark/ark-components/ark-component-common/ark-component-common-core/src/test/resources/code.txt";
        List<String> strings = FileUtil.readLines(fileName, Charset.defaultCharset());
        String collect = strings.stream().map(item -> "'" + item + "'").collect(Collectors.joining(","));
        String s = "select * from us_button where code in (%s)".formatted(collect);
        System.out.println(s);
    }

    @Test
    public void simpleRead() {
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1
        String fileName = "/Users/chenjiawei/code/myself/ark/ark-components/ark-component-common/ark-component-common-core/src/test/resources/demo.xlsx";
        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
//
//        EasyExcel.read(fileName, Sheet1.class, new DemoDataListener("sheet1.sql"))
//                .excelType(ExcelTypeEnum.XLSX)
//                .sheet(0)
//                .doRead();

        EasyExcel.read(fileName, Sheet2.class, new Demo2DataListener("sheet2.sql"))
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(1)
                .doRead();
    }
}
