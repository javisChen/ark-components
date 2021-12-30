package com.kt.component.exception.handler;

import com.kt.component.dto.ResponseEnums;
import com.kt.component.dto.ServerResponse;
import com.kt.component.dto.SingleResponse;
import com.kt.component.exception.BizException;
import com.kt.component.validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JavisChen
 * 统一异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handle(Exception e) {
        log.error("SYS EXCEPTION：", e);
        return ServerResponse.error(ResponseEnums.SERVER_ERROR);
    }


    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handle(BizException e) {
        log.error("BIZ EXCEPTION：", e);
        return ServerResponse.error(e.getErrCode(), e.getMessage());
    }

    /**
     * HTTP METHOD不匹配异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ServerResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ServerResponse.error(ResponseEnums.USER_METHOD_NOT_ALLOWED);
    }

    /*
     * @desc 处理参数校验异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ValidationResult.FieldError> validationResultErrors = new ArrayList<>();
            fieldErrors.forEach((error -> validationResultErrors
                    .add(new ValidationResult.FieldError(error.getField(), error.getDefaultMessage()))));
            return SingleResponse.error(ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID.getCode(),
                    ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID.getMsg(),
                    new ValidationResult(bindingResult.getFieldError().getDefaultMessage(), validationResultErrors));
        }
        return SingleResponse.error(ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID);
    }

    /*
     * @desc 处理参数校验异常
     * @param [e]
     */
//    @ExceptionHandler(value = ClientArgumentNotValidException.class)
//    public ServerResponse handle(ClientArgumentNotValidException e) {
//        List<ValidationResult.FieldError> validationResultErrors = new ArrayList<>();
//        validationResultErrors.add(new ValidationResult.FieldError(e.getErrorField(), e.getErrorMsg()));
//        return ServerResponse.error(ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID.getCode(), ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID.getMsg(), new ValidationResult(e.getErrorMsg(), validationResultErrors));
//    }

    /**
     * 处理参数校验异常（post application/x-www-form-urlencoded）就会抛出 BindException
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SingleResponse<ValidationResult> handle(BindException e) {
        List<ValidationResult.FieldError> validationResultErrors = new ArrayList<>();
        e.getFieldErrors()
                .forEach((error) -> validationResultErrors
                        .add(new ValidationResult.FieldError(error.getField(), error.getDefaultMessage())));
        return SingleResponse.error(ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID.getCode(),
                ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID.getMsg(),
                new ValidationResult(e.getBindingResult().getFieldError().getDefaultMessage(), validationResultErrors));
    }

    /**
     * 处理RequestParam的校验异常
     * @param e
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SingleResponse<ValidationResult> handle(ConstraintViolationException e) {
        ValidationResult result = new ValidationResult(
                e.getConstraintViolations().stream().findFirst().get().getMessage(), null);
        return SingleResponse.error(ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID, result);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handle(HttpMessageNotReadableException e) {
        if (e.getMessage().contains("JSON parse error: 2")) {
            return ServerResponse.error(ResponseEnums.USER_METHOD_ARGUMENT_NOT_VALID);
        }
        return ServerResponse.error(ResponseEnums.USER_REQUIRED_REQUEST_BODY_IS_MISSING);
    }

    /**
     * 调用上传接口没有传入文件
     *
     * @return httpStatus=400 busiCode=-1
     */
    @ExceptionHandler(value = {
            org.springframework.web.multipart.support.MissingServletRequestPartException.class,
            org.springframework.web.multipart.MultipartException.class
    })
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handleMissingServletRequestPartException(Exception e) {
        return ServerResponse.error("上传文件操作异常：{}", e.getMessage());
    }
}
