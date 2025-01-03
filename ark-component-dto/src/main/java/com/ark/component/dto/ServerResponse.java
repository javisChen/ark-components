package com.ark.component.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 服务端响应结果封装
 *
 * @author JavisChen
 */
@Schema(description = "服务端统一响应结果")
@Data
@ToString
@Accessors(chain = true)
public class ServerResponse {

    @Schema(description = "响应码（0表示成功）",
            example = "0",
            title = "响应码",
            defaultValue = "0")
    private String code;
    
    @Schema(description = "响应消息", 
            example = "操作成功",
            title = "响应消息")
    private String msg;
    
    @Schema(description = "响应服务名称", 
            example = "user-service",
            title = "服务名称")
    private String service;
    
    @Schema(description = "链路追踪ID", 
            example = "trace-id-123456",
            title = "链路ID")
    private String traceId;

    public ServerResponse() {

    }

    public ServerResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ServerResponse ok() {
        return createResponse(BizErrorCode.OK.getCode(), BizErrorCode.OK.getMsg());
    }

    public static ServerResponse error(BizErrorCode responseEnums) {
        return createResponse(responseEnums.getCode(), responseEnums.getMsg());
    }

    public static ServerResponse error(String code, String msg) {
        return createResponse(code, msg);
    }

    public static ServerResponse error(String service, String code, String msg) {
        return createResponse(service, code, msg);
    }

    protected static ServerResponse createResponse(String code, String msg) {
        return createResponse(null, code, msg);
    }

    protected static ServerResponse createResponse(String service, String code, String msg) {
        return new ServerResponse()
                .setCode(code)
                .setMsg(msg);
//                .setService(StringUtils.defaultString(service, SpringUtils.getApplicationName()));
    }

}
