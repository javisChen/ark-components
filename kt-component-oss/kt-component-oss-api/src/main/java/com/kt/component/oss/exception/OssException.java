package com.kt.component.oss.exception;

import com.kt.component.exception.BizException;

/**
 * OSS操作异常类
 * @author victor
 */
public class OssException extends BizException {

    public OssException(String errMessage) {
        super(errMessage);
    }

    public OssException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public OssException(String errMessage, Throwable e) {
        super(errMessage, e);
    }

    public OssException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}
