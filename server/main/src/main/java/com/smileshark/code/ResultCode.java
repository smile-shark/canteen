package com.smileshark.code;

public enum ResultCode {
    SUCCESS(200, "成功"),
    FAILED(500, "失败"),
    PARAM_ERROR(501, "参数错误"),
    DB_ERROR(502, "数据库错误"),
    UNKNOWN_ERROR(500, "未知错误"),
    EMAIL_SEND_ERROR(503, "邮件发送失败"),
    NOT_HAVE_ACCOUNT(504, "没有该账号"),
    PASSWORD_ERROR(505,"密码错误" ), ADD_ERROR(506, "添加失败"), DELETE_ERROR(507, "删除失败"), DELETE_SUCCESS(200 , "删除成功"), UPDATE_SUCCESS(200, "更新成功"), UPDATE_ERROR(508, "更新失败"), ADD_SUCCESS(200, "添加成功"), PERMISSION_ERROR(509, "没有权限"), VERIFY_CODE_ERROR(510, "验证码错误"), VERIFY_CODE_EXPIRED(511, "验证码过期"), VERIFY_CODE_SEND_ERROR(512, "验证码发送失败"), VERIFY_CODE_SEND_SUCCESS(200, "验证码发送成功"), VERIFY_CODE_CHECK_SUCCESS(200, "验证码验证成功"), VERIFY_CODE_CHECK_ERROR(513, "验证码验证失败"), VERIFY_CODE_CHECK_EXPIRED(514, "验证码验证过期"), VERIFY_CODE_CHECK_ERROR_COUNT(515, "验证码验证错误次数过多"), VERIFY_CODE_CHECK_ERROR_LOCK(516, "验证码验证锁定"), VERIFY_CODE_CHECK_ERROR_USER(517, "验证码验证用户错误"), VERIFY_CODE_CHECK_ERROR_CODE(518, "验证码验证验证码错误"), EMAIL_FORMAT_ERROR(519, "邮箱格式错误"), EMAIL_EXIST(520, "邮箱已存在"), EMAIL_NOT_EXIST(521, "邮箱不存在"), EMAIL_SEND_SUCCESS(200, "邮件发送成功"), EMAIL_SEND_ERROR_COUNT(522, "邮件发送失败次数过多"), EMAIL_SEND_ERROR_LOCK(523, "邮件发送锁定"), EMAIL_SEND_ERROR_USER(524, "邮件发送用户错误"), EMAIL_SEND_ERROR_CODE(525, "邮件发送验证码错误"), EMAIL_SEND_ERROR_EMAIL(526, "邮件发送邮箱错误"), TOKEN_ERROR(401,"身份验证失败" ), NUM_NOT_ENOUGH(527, "数量不足"), NOT_HAVE_ORDER(528, "没有发现订单"), NOT_HAVE_CUISINE(529, "没有该菜品"), COUPON_NOT_EXIST(530, "优惠券不存在"), COUPON_EXPIRED(531,"优惠券已过期" ), COUPON_NOT_ENOUGH(532, "数量不足"), SEND_COUPON_SUCCESS(200, "发放成功"), NOT_HAVE_WALLET(533, "没有钱包"), NOT_ENOUGH_MONEY(534, "余额不足"), NOT_HAVE_DISCOUNT_COUPON(535, "没有该优惠券"), COUPON_USED(536, "优惠券已经被使用过"), RECHARGE_SUCCESS(200, "充值成功");


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
