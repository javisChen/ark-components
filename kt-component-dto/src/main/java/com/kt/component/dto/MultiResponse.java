package com.kt.component.dto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MultiResponse<T> extends ServerResponse {

    private static final long serialVersionUID = 1L;

    private Collection<T> data;

    public List<T> getData() {
        return null == data ? Collections.emptyList() : new ArrayList<>(data);
    }

    public void setData(Collection<T> data) {
        this.data = data;
    }

    public static <T> MultiResponse<T> ok(Collection<T> data) {
        MultiResponse<T> response = new MultiResponse<>();
        response.setData(data);
        response.setCode(ResponseEnums.OK.getCode());
        response.setMsg(ResponseEnums.OK.getMsg());
        return response;
    }

    public static <T> MultiResponse<T> error(String code, String msg, Collection<T> data) {
        MultiResponse<T> response = new MultiResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

}
