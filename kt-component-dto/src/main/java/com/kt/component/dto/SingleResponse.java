package com.kt.component.dto;
import T;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/*
 * @author JavisChen
 * @desc 服务端响应结果封装
 * @date 2018/4/17 下午10:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class SingleResponse<T> extends ServerResponse {

    private static final long serialVersionUID = -5409913864886373072L;
    private T data;


    public static <T> SingleResponse<T> ok(T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setData(data);
        response.setCode(ResponseEnums.OK.getCode());
        response.setMsg(ResponseEnums.OK.getMsg());
        return response;
    }

    public static <T> SingleResponse<T> error(ResponseEnums responseEnums, T data) {
        SingleResponse<T> response = new SingleResponse<>();
        response.setData(data);
        response.setCode(responseEnums.getCode());
        response.setMsg(responseEnums.getMsg());
        return response;
    }


//    public static <T> SingleResponse<T> error(String code, String msg, T data) {
//        return new SingleResponse<T>().setCode(code).setMsg(msg).setData(data);
//    }
}
