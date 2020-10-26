package com.meiyuan.catering.allinpay.enums;

/**
 * created on 2020/8/14 13:57
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum CommonReturnCodeEnums {
    /** */
    SUCCESS("10000","接口调用成功"),
    /** {@link ServiceUnavailableCodeEnums} */
    SERVICE_UNAVAILABLE("20000","服务不可用"),
    /** {@link ServiceResultCodeEnums} */
    FAILURE("40004","业务处理失败")
    ;
    private String code;
    private String msg;
    CommonReturnCodeEnums(String code,String msg){
        this.code = code;
        this.msg  = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
