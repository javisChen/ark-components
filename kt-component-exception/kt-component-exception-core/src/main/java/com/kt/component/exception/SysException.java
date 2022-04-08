package com.kt.component.exception;

/**
 * 系统内部异常
 * @author JavisChen
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 4355163994767354840L;

    private static final String DEFAULT_ERR_CODE = "S0001";

    public SysException(String errMessage) {
        super(DEFAULT_ERR_CODE, errMessage);
    }

    public SysException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public SysException(String errMessage, Throwable e) {
        super(DEFAULT_ERR_CODE, errMessage, e);
    }

    public SysException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}
