package com.ark.component.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 集合对象响应结果封装
 *
 * @author jc
 * @param <T> 集合元素类型
 */
@Schema(description = "集合对象响应结果")
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class MultiResponse<T> extends ServerResponse {

    @Schema(description = "响应数据集合",
            title = "数据集合")
    private Collection<T> data;

    public MultiResponse(String code, String msg, Collection<T> data) {
        super(code, msg);
        this.data = data;
    }

    public List<T> getData() {
        return null == data ? Collections.emptyList() : new ArrayList<>(data);
    }

    public static <T> MultiResponse<T> ok(Collection<T> data) {
        return new MultiResponse<>(BizErrorCode.OK.getCode(), BizErrorCode.OK.getMsg(), data);
    }

    public static <T> MultiResponse<T> error(String code, String msg, Collection<T> data) {
        return new MultiResponse<>(code, msg, data);
    }

}
