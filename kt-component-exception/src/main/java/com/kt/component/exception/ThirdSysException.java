package com.kt.component.exception;

/**
 * 第三方服务异常
 * @author JavisChen
 */
public class ThirdSysException extends BaseException {

    private static final long serialVersionUID = 4355163994767354840L;

    private static final String DEFAULT_ERR_CODE = "T0001";

    public ThirdSysException(String errMessage) {
        super(DEFAULT_ERR_CODE, errMessage);
    }

    public ThirdSysException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public ThirdSysException(String errMessage, Throwable e) {
        super(DEFAULT_ERR_CODE, errMessage, e);
    }

    public ThirdSysException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}
