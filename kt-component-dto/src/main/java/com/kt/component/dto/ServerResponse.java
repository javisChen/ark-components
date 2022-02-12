package com.kt.component.dto;

import com.kt.component.context.ServiceContext;
import com.kt.component.web.util.SpringUtils;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*
 * @author JavisChen
 * @desc 服务端响应结果封装
 * @date 2018/4/17 下午10:24
 */
@Data
@ToString
@Accessors(chain = true)
public class ServerResponse implements Serializable {

    private static final long serialVersionUID = -5409913864886373072L;
    private String code;
    private String msg;
    private String service;
    private String traceId;

    public ServerResponse() {

    }

    public ServerResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.service = SpringUtils.getApplicationName();
        this.traceId = ServiceContext.getTraceId();
    }

    public static ServerResponse ok() {
        return createResponse(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMsg());
    }

    public static ServerResponse error(ResponseEnums responseEnums) {
        return createResponse(responseEnums.getCode(), responseEnums.getMsg());
    }

    public static ServerResponse error(String code, String msg) {
        return createResponse(code, msg);
    }

    protected static ServerResponse createResponse(String code, String msg) {
        return new ServerResponse()
                .setCode(code)
                .setMsg(msg)
                .setService(SpringUtils.getApplicationName());
    }

}
