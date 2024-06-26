package com.ark.component.exception;

import java.util.function.Supplier;

/**
 * @ Description   :  异常工厂实现
 * @ Author        :  JavisChen
 * @ CreateDate    :  2020/04/11
 * @ Version       :  1.0
 */
public class ExceptionFactory {

    /**
     * 系统内部异常
     * @param errorMessage 具体错误信息
     * @return UserException
     */
    public static SysException sysException(String errorMessage) {
        return new SysException(errorMessage);
    }

    /**
     * 系统内部异常
     * @param errorMessage 具体错误信息
     * @return UserException
     */
    public static Supplier<RuntimeException> sysExceptionSupplier(String errorMessage) {
        return () -> new SysException(errorMessage);
    }

    public static SysException sysException(String errorCode, String errorMessage) {
        return new SysException(errorCode, errorMessage);
    }

    public static SysException sysException(String errorMessage, Throwable e) {
        return new SysException(errorMessage, e);
    }

    public static SysException sysException(String errorCode, String errorMessage, Throwable e) {
        return new SysException(errorCode, errorMessage, e);
    }

    /**
     * 用户操作异常
     * @param errorMessage 具体错误信息
     * @return UserException
     */
    public static UserException userException(String errorMessage) {
        return new UserException(errorMessage);
    }

    /**
     * 用户操作异常
     * @param errorMessage 具体错误信息
     * @return UserException
     */
    public static Supplier<RuntimeException> userExceptionSupplier(String errorMessage) {
        return () -> new UserException(errorMessage);
    }

    /**
     * 第三方服务异常
     * @param errorMessage 具体错误信息
     * @return ThirdSysException
     */
    public static ThirdSysException thirdSysException(String errorMessage) {
        return new ThirdSysException(errorMessage);
    }

    /**
     * 第三方服务异常
     * @param errorMessage 具体错误信息
     * @param e 错误堆栈
     * @return ThirdSysException
     */
    public static ThirdSysException thirdSysException(String errorMessage, Throwable e) {
        return new ThirdSysException(errorMessage, e);
    }
    /**
     * 第三方服务异常
     * @param errorMessage 具体错误信息
     * @return RpcException
     */
    public static RpcException rpcException(String errorMessage) {
        return new RpcException(errorMessage);
    }

    /**
     * 第三方服务异常
     * @param service 报错的服务名
     * @return RpcException
     */
    public static RpcException rpcException(String service, String message) {
        return new RpcException(service, message);
    }

    /**
     * 第三方服务异常
     * @param errorMessage 具体错误信息
     * @param e 错误堆栈
     * @return RpcException
     */
    public static RpcException rpcException(String errorMessage, Throwable e) {
        return new RpcException(errorMessage, e);
    }

    /**
     * 第三方服务异常
     */
    public static RpcException rpcException(String service, Object response, String message, String bizErrorCode) {
        return new RpcException(service, response, message, bizErrorCode);
    }


}
