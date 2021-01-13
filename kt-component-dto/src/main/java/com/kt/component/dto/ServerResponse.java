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
public class ServerResponse implements Serializable {

    private static final long serialVersionUID = -5409913864886373072L;
    private String code;
    private String msg;

    public ServerResponse() {

    }

    public ServerResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ServerResponse ok() {
        return new ServerResponse().setCode(ResponseEnums.OK.getCode()).setMsg(ResponseEnums.OK.getMsg());
    }

    public static ServerResponse error(ResponseEnums responseEnums) {
        return new ServerResponse().setCode(responseEnums.getCode()).setMsg(responseEnums.getMsg());
    }

    public static ServerResponse error(String code, String msg) {
        return new ServerResponse().setCode(code).setMsg(msg);
    }

}
