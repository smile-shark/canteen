package com.smileshark.exception;

import com.smileshark.code.ResultCode;

public class BusinessException extends RuntimeException {
    private final ResultCode rCode;

    public BusinessException(ResultCode rCode) {
        super(rCode.getMsg());
        this.rCode = rCode;
    }

    public ResultCode getRCode() {
        return rCode;
    }
}
