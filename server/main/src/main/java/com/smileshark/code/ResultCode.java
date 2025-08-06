package com.smileshark.code;

public enum ResultCode {
    SUCCESS(200, "成功"),
    FAILED(500, "失败"),
    PARAM_ERROR(501, "参数错误"),
    DB_ERROR(502, "数据库错误"),
    UNKNOWN_ERROR(500, "未知错误"),
    EMAIL_SEND_ERROR(503, "邮件发送失败"),
    NOT_HAVE_ACCOUNT(504, "没有该账号"),
    PASSWORD_ERROR(505,"密码错误" ), ADD_ERROR(506, "添加失败"), DELETE_ERROR(507, "删除失败"), DELETE_SUCCESS(200 , "删除成功"), UPDATE_SUCCESS(200, "更新成功"), UPDATE_ERROR(508, "更新失败"), ADD_SUCCESS(200, "添加成功"), PERMISSION_ERROR(509, "没有权限");


    private int code;
    private String msg;

    ResultCode(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
