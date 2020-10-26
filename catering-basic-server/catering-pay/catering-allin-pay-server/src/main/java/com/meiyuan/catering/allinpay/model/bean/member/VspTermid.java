package com.meiyuan.catering.allinpay.model.bean.member;

import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/10/17 11:38
 * @since v1.5.0
 */
@Data
public class VspTermid {

    /**
     * 收银宝集团商户号 集团模式：集团商户收银宝商户号 单商户模式：不返
     */
    private String vspMerchantid;
    /**
     * 收银宝商户号 单商户模式：商户收银宝商户号 集团模式：收银宝子商户号
     */
    private String vspCusid;
    /**
     * 收银宝终端号
     */
    private String vspTermid;
    /**
     * 绑定时间，yyyy-MM-dd HH:mm:ss
     */
    private String setDate;
}
