package com.meiyuan.catering.allinpay.enums;

/**
 * created on 2020/8/14 14:04
 *
 * @author yaozou
 * @since v1.0.0
 */
public enum ServiceUnavailableCodeEnums {
    /**  */
    MESSAGE_FORMAT_ERROR("GW0001"," 报文格式错误"),
    CANNOT_DECRYPT("GW0002","bizContent 不能解密"),
    ENCRYPT_FAILURE("GW0003","加密 bizContent 失败"),
    /** 1.公私钥是否是一对 2.检查公钥上传是否与私钥 匹配 3.存在中文需要做 urlencode 4.签名算法是否无误 */
    SIGN_ERROR("GW0004","客户签名错误"),
    INVOKE_API_ERROR("GW0005","接口调用错误"),
    PUBLIC_KEY_NOT_EXIST("GW0006","公钥不存在"),
    TRANSFER_DATA_ERROR("GW0007","数据传输错误"),
    /** 请检查配置的应用是否有当 前接口权限 */
    NO_ACCESS("GW0008"," 没有访问权限"),
    PARSE_MESSAGE_FAILED("GW0009","解析报文失败"),
    QUERY_MERCHANT_PUBLIC_KEY_ERROR("GW0010","查询商户公钥失败"),
    TRANSACTION_PATH_NOT_EXIST("GW0011","交易路径不存在"),
    SEND_RETURN_MSG_ERROR("GW0012"," 发送返回报文错误"),
    SYSTEM_ERROR("GW0013","系统错误"),
    TRANSACTION_TIMEOUT("GW0014","交易超时"),
    RESTRICTED_ACCESS_FREQUENCY("GW0015"," 访问频率受限"),
    ;
    private String code;
    private String msg;
    ServiceUnavailableCodeEnums(String code,String msg){
        this.code = code;
        this.msg  = msg;
    }
}
