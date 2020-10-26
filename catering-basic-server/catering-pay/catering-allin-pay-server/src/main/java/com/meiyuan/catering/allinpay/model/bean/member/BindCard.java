package com.meiyuan.catering.allinpay.model.bean.member;

import lombok.Data;

@Data
public class BindCard {
    /**
     * 商户系统用户标识，商户系统中唯一编号
     */
    private String bizUserId;
    /**
     * 银行卡号 AES 加密。
     */
    private String bankCardNo;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 绑定时间，yyyy-MM-dd HH:mm:ss
     */
    private String bindTime;
    /**
     * 银行卡类型 1-借记卡 2-信用卡
     */
    private Integer cardType;
    /**
     * 绑定状态 1-已绑定 2-已解除
     */
    private Integer bindState;
    /**
     * 银行预留手机号码（仅四要素绑定的银行卡返回）
     */
    private String phone;
    /**
     * 是否安全卡：true,false
     */
    private Boolean isSafeCard;
    /**
     * 银行卡/账户属性 0：个人银行卡 1：企业对公账户
     */
    private Integer bankCardPro;
}
