package com.smileshark.common;

import com.smileshark.code.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String msg;
    private T data;
    
    // Success methods
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), data);
    }
    
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), msg, data);
    }
    
    // Error methods
    public static <T> Result<T> error() {
        return new Result<>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMsg(), null);
    }
    
    public static <T> Result<T> error(String msg) {
        return new Result<>(ResultCode.FAILED.getCode(), msg, null);
    }
    
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }
    
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public Result<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}