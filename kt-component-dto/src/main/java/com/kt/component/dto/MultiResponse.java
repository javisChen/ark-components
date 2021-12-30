package com.kt.component.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 响应类型为Collection的响应体
 * @param <T>
 */
public class MultiResponse<T> extends ServerResponse {

    private static final long serialVersionUID = 1L;

    private Collection<T> data;

    public MultiResponse(String code, String msg, Collection<T> data) {
        super(code, msg);
        this.data = data;
    }

    public List<T> getData() {
        return null == data ? Collections.emptyList() : new ArrayList<>(data);
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public static <T> MultiResponse<T> ok(Collection<T> data) {
        return new MultiResponse<>(ResponseEnums.OK.getCode(), ResponseEnums.OK.getMsg(), data);
    }

    public static <T> MultiResponse<T> error(String code, String msg, Collection<T> data) {
        return new MultiResponse<>(code, msg, data);
    }

}
