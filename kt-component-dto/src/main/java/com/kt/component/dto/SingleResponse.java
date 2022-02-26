package com.kt.component.dto;

import lombok.*;
import lombok.experimental.Accessors;

/*
 * @author jc
 * @desc 服务端响应结果封装
 * @date 2018/4/17 下午10:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class SingleResponse<T> extends ServerResponse {

    private static final long serialVersionUID = -5409913864886373072L;

    private T data;

    public SingleResponse(String code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public static <T> SingleResponse<T> ok(T data) {
        return new SingleResponse<>(BizErrorCode.OK.getCode(), BizErrorCode.OK.getMsg(), data);
    }

    public static <T> SingleResponse<T> error(BizErrorCode responseEnums, T data) {
        return new SingleResponse<>(responseEnums.getCode(), responseEnums.getMsg(), data);
    }

    public static <T> SingleResponse<T> error(String code, String msg, T data) {
        return new SingleResponse<>(code, msg, data);
    }

}
