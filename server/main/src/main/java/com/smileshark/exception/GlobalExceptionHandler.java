package com.smileshark.exception;

import com.smileshark.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error();
    }
    @ExceptionHandler({BusinessException.class})
    public Result<?> handlerBusinessException(BusinessException e) {
        return Result.error(e.getRCode());
    }
}
