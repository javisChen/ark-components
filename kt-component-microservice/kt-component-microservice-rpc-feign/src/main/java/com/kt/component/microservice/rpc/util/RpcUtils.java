package com.kt.component.microservice.rpc.util;

import com.kt.component.dto.MultiResponse;
import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.microservice.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * RPC辅助工具类
 * @author javischen
 */
@Slf4j
public class RpcUtils {

    /**
     * 如果服务器响应错误或异常的话，会抛出RpcException
     * @param serverResponse 服务端响应体
     * @return List
     */
    public static <T> T attemptGetData(SingleResponse<T> serverResponse) {
        checkResponse(serverResponse);
        return serverResponse.getData();
    }

    /**
     * 如果服务器响应错误或异常的话，会抛出RpcException
     * @param serverResponse 服务端响应体
     * @return List
     */
    public static <T> List<T> attemptGetData(MultiResponse<T> serverResponse) {
        checkResponse(serverResponse);
        return serverResponse.getData();
    }

    /**
     * 如果服务器响应错误或异常的话，打印错误日志，但是不会抛出异常，返回null
     * @param serverResponse 服务端响应体
     * @return List
     */
    public static <T> List<T> getData(MultiResponse<T> serverResponse) {
        return isSuccess(serverResponse) ? serverResponse.getData() : null;
    }

    private static <T> void checkResponse(ServerResponse serverResponse) {
        if (serverResponse == null) {
            log.error("[RPC]调用异常 -> 响应结果为空");
            throw new RpcException("[RPC]调用异常 -> 响应结果为空");
        }
        if (!serverResponse.getCode().equals(ResponseEnums.OK.getCode())) {
            log.error("[RPC]调用返回错误：" + serverResponse);
            String msg = serverResponse.getMsg();
            throw new RpcException(msg);
        }
    }

    public static boolean isSuccess(ServerResponse serverResponse) {
        return Objects.nonNull(serverResponse) &&
                serverResponse.getCode().equals(ResponseEnums.OK.getCode());
    }
}
