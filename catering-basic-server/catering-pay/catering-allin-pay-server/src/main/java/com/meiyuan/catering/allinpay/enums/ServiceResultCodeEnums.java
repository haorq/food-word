package com.meiyuan.catering.allinpay.enums;

/**
 * created on 2020/8/14 14:22
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum ServiceResultCodeEnums {
    /**
     *
     */
    SUCCESS("OK", "服务调用成功"),

    /**
     * 系统类错误
     */
    PARAM_ERROR("10000", "参数错误"),
    PARAM_IS_EMPTY("10001", "参数不能为空"),
    PARAM_SERVICE_ERROR("10002", "参数 service 错误"),
    PARAM_METHOD_ERROR("10003", "参数 method 错误"),
    PARAM_LENGTH_OUT("10004", "参数长度超长"),

    /**
     * 应用类错误
     */
    PASSWORD_ERROR("20010", "密码错误"),
    VERIFICATION_CODE_ERROR("20011", "验证码错误"),
    BANK_NOT_SUPPORT_WITHDRAWAL("20020", "该银行卡不支持提现或代付/解码错误"),

    /**
     * 用户类错误
     */
    USER_EXIST("30000", "用户已经存在"),
    USER_NOT_EXIST("30001", "用户不存在"),
    /**
     * 订单类错误
     */
    ORDER_NOT_EXIST("40000", "订单不存在"),
    PAY_FAIL("40006", "支付失败"),
    BALANCE_NO_ENOUGH("40014", "账户余额不足"),
    ORDER_EXIST("40017", "订单已经存在"),

    RESTRICT_NUMBER("40045", "风控超限单日累计笔数"),
    RESTRICT_AMOUNT("40046", "风控限制订单单笔金额"),
    RESTRICT_TOTAL_AMOUNT("40047", "风控超限单日累计金额"),

    /**
     * 账户类错误
     */
    ACCOUNT_BALANCE_NO_ENOUGH("50001", "账户余额不足"),
    ACCOUNT_BROKEN("50002", "账户已冻结"),
    ACCOUNT_NOT_EXIST("50006", "账户不存在"),
    ;
    private String code;
    private String msg;

    ServiceResultCodeEnums(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static ServiceResultCodeEnums toEnum(String code) {
        ServiceResultCodeEnums[] enums = ServiceResultCodeEnums.values();
        for (ServiceResultCodeEnums enum_ : enums) {
            if (enum_.getCode().equals(code)) {
                return enum_;
            }
        }
        return null;
    }

}
