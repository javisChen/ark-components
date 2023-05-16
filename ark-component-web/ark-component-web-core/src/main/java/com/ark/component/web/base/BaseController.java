package com.ark.component.web.base;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public abstract class BaseController {

    protected HttpSession getSession() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest().getSession();
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }

    public HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getResponse();
    }

    /**
//     * 导出文件（基于Bean）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @throws IOException
//     */
//    protected void export(String filename, List<?> list, String[] headers, String[] fields) throws IOException {
//        ExcelUtils.exportToResp(filename, list, headers, fields);
//    }
//
//    /**
//     * 导出文件（基于Bean）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @param sheetName 表名
//     * @throws IOException
//     */
//    protected void export(String filename, List<?> list, String[] headers, String[] fields, String sheetName) throws IOException {
//        ExcelUtils.exportToResp(filename, list, headers, fields, sheetName);
//    }
//
//    /**
//     * 导出文件（基于List<Map>）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @throws IOException
//     */
//    protected void exportFromMap(String filename, List<Map<String, Object>> list, String[] headers, String[] fields) throws IOException {
//        ExcelUtils.exportMapToResp(filename, list, headers, fields);
//    }
//
//    /**
//     * 导出文件（基于List<Map>）
//     * @param filename 文件名
//     * @param list 导出数据
//     * @param headers excel头
//     * @param fields 数据对应的字段
//     * @param sheetName 表名
//     * @throws IOException
//     */
//    protected void exportFromMap(String filename, List<?> list, String[] headers, String[] fields, String sheetName) throws IOException {
//        ExcelUtils.exportMapToResp(filename, list, headers, fields, sheetName);
//    }
//
//    /**
//     * 导出模板
//     * @param filename 文件名
//     * @param headers excel头
//     */
//    protected void exportTemplate(String filename, String[] headers) throws IOException {
//        ExcelUtils.exportMapToResp(filename, null, headers,null, "模板");
//    }

}
