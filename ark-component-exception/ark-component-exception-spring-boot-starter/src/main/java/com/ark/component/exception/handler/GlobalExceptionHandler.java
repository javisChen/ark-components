package com.ark.component.exception.handler;

import com.ark.component.dto.BizErrorCode;
import com.ark.component.dto.ServerResponse;
import com.ark.component.dto.SingleResponse;
import com.ark.component.exception.*;
import com.ark.component.validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

import jakarta.validation.ConstraintViolationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ark.component.dto.BizErrorCode.USER_ERROR;

/**
 * @author JavisChen
 * 统一异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerResponse handle(Exception e) {
        log.error("Unknown exception：", e);
        return ServerResponse.error(BizErrorCode.SERVER_ERROR);
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ServerResponse handle(NoResourceFoundException e) {
        return ServerResponse.error(USER_ERROR);
    }

    @ExceptionHandler(value = RpcException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerResponse handle(RpcException e) {
        log.error("Rpc exception：", e);
        return ServerResponse.error(BizErrorCode.RPC_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerResponse handle(UserException e) {
        log.warn("User exception：", e);
        return ServerResponse.error(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(value = ThirdSysException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerResponse handle(ThirdSysException e) {
        log.error("Third service exception：", e);
        return ServerResponse.error(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(value = SysException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ServerResponse handle(SysException e) {
        log.error("sys exception：", e);
        return ServerResponse.error(e.getErrCode(), e.getMessage());
    }

    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handle(BizException e) {
        log.error("Biz Exception：", e);
        return ServerResponse.error(e.getErrCode(), e.getMessage());
    }

    /**
     * HTTP METHOD不匹配异常
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ServerResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ServerResponse.error(USER_ERROR.getCode(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    /**
     * 如果入参是@RequestBody + @Validated
     * JSR303参数校验不通过就是抛出MethodArgumentNotValidException
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerResponse handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ValidationResult.FieldError> validationResultErrors = new ArrayList<>(fieldErrors.size());
            fieldErrors.forEach((error -> validationResultErrors
                    .add(new ValidationResult.FieldError(error.getField(), error.getDefaultMessage()))));
            FieldError fieldError = bindingResult.getFieldError();
            String defaultMessage = "";
            if (Objects.nonNull(fieldError)) {
                defaultMessage = fieldError.getDefaultMessage();
            }
            return SingleResponse.error(USER_ERROR.getCode(), defaultMessage,
                    new ValidationResult(defaultMessage, validationResultErrors));
        }
        return SingleResponse.error(USER_ERROR);
    }

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
        return SingleResponse.error(USER_ERROR.getCode(),
                USER_ERROR.getMsg(),
                new ValidationResult(e.getBindingResult().getFieldError().getDefaultMessage(), validationResultErrors));
    }

    /**
     * 处理RequestParam的校验异常
     * @param e ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public SingleResponse<ValidationResult> handle(ConstraintViolationException e) {
        ValidationResult result = new ValidationResult(
                e.getConstraintViolations().stream().findFirst().get().getMessage(), null);
        return SingleResponse.error(USER_ERROR.getCode(), result.getDefaultMsg(), result);
    }

    /**
     * RequestBody解析失败，例如：请求体为空、类型不匹配等
     * @param e HttpMessageNotReadableException
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ServerResponse handle(HttpMessageNotReadableException e) {
        String message = e.getMessage();
        if (StringUtils.isNotBlank(message)) {
            if (message.contains("JSON parse error")) {
                return ServerResponse.error(USER_ERROR.getCode(), e.getMessage());
            } else if (message.contains("Required request body is missing")) {
                return ServerResponse.error(USER_ERROR.getCode(), "Required request body is missing");
            }
        }
        return ServerResponse.error(USER_ERROR.getCode(), message);
    }

    /**
     * 调用上传接口没有传入文件
     * @return httpStatus=400 busiCode=-1
     */
    @ExceptionHandler(value = {
            org.springframework.web.multipart.support.MissingServletRequestPartException.class,
            org.springframework.web.multipart.MultipartException.class
    })
    @ResponseStatus(HttpStatus.OK)
    public ServerResponse handleMissingServletRequestPartException(Exception e) {
        return ServerResponse.error(USER_ERROR.getCode(), "上传文件操作异常：{}", e.getMessage());
    }
}
