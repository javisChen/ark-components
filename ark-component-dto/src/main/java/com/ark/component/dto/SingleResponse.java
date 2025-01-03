package com.ark.component.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 单对象响应结果封装
 *
 * @author jc
 */
@Schema(description = "单对象响应结果")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class SingleResponse<T> extends ServerResponse {

    @Schema(description = "响应数据对象",
            title = "数据对象")
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
