package com.ark.component.exception;

/**
 * 用户操作系统
 * @author JavisChen
 */
public class UserException extends BaseException {

    private static final long serialVersionUID = 4355163994767354840L;

    private static final String DEFAULT_ERR_CODE = "U0001";

    public UserException(String errMessage) {
        super(DEFAULT_ERR_CODE, errMessage);
    }

    public UserException(String errCode, String errMessage) {
        super(errCode, errMessage);
    }

    public UserException(String errMessage, Throwable e) {
        super(DEFAULT_ERR_CODE, errMessage, e);
    }

    public UserException(String errorCode, String errMessage, Throwable e) {
        super(errorCode, errMessage, e);
    }

}
