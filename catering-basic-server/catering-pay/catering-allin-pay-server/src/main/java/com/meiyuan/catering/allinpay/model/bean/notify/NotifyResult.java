package com.meiyuan.catering.allinpay.model.bean.notify;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.meiyuan.catering.allinpay.enums.service.notify.NotifyTypeEnums;
import com.meiyuan.catering.core.exception.CustomException;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/9/30 13:49
 * @since v1.5.0
 */
@Data
public class NotifyResult {
    /**
     * 通商云分配给开发者的应用ID
     */
    private String appId;
    /**
     * 通知的类型
     */
    private String notifyType;
    /**
     * 通知的发送时间。格式为yyyy-MM-dd HH:mm:ss
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime notifyTime;
    /**
     * 通知校验ID
     */
    private String notifyId;
    /**
     * 编码格式，utf-8
     */
    private String charset;
    /**
     * 调用的接口版本
     */
    private String version;
    /**
     * 签名算法类型，目前支持SHA256WithRSA
     */
    private String signType;
    /**
     * 签名串
     */
    private String sign;
    /**
     * 通知参数的集合，最大长度不限，除公共参数外所有通知参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     */
    private String bizContent;

    public <T> T getResult() {
        NotifyTypeEnums typeEnums = NotifyTypeEnums.match(notifyType);

        switch (typeEnums) {
            case ORDER_NOTIFY:
                OrderNotifyResult orderNotifyResult = JSON.parseObject(bizContent, OrderNotifyResult.class);
                return (T) orderNotifyResult;
            case SIGN_CONTRACT_NOTIFY:
                SignContractNotifyResult signContractNotifyResult = JSON.parseObject(bizContent, SignContractNotifyResult.class);
                return (T) signContractNotifyResult;
            case MEMBER_COMPANY_NOTIFY:
                return (T) JSON.parseObject(bizContent);
            case WITHDRAW_APPLY_NOTIFY:
                WithdrawNotifyResult withdrawNotifyResult = JSON.parseObject(bizContent, WithdrawNotifyResult.class);
                return (T) withdrawNotifyResult;
            default:
                throw new CustomException("notifyType 不匹配");
        }
    }

}
