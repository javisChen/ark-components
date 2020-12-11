package com.kt.component.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = -5409913864886373072L;
    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public static ServerResponse ok() {
        return new ServerResponse<>().setCode(ResponseEnums.OK.getCode()).setMsg(ResponseEnums.OK.getMsg());
    }

    public static <T> ServerResponse ok(T data) {
        return new ServerResponse<T>().setCode(ResponseEnums.OK.getCode()).setMsg(ResponseEnums.OK.getMsg()).setData(data);
    }

    public static ServerResponse<String> error(ResponseEnums responseEnums) {
        return new ServerResponse<String>().setCode(responseEnums.getCode()).setMsg(responseEnums.getMsg());
    }

    public static ServerResponse<String> error(String code, String msg) {
        return new ServerResponse<String>().setCode(code).setMsg(msg);
    }

    public static <T> ServerResponse<T> error(String code, String msg, T data) {
        return new ServerResponse<T>().setCode(code).setMsg(msg).setData(data);
    }
}
