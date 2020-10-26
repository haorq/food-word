package com.meiyuan.catering.allinpay.model.bean.order;

import lombok.Data;

/**
 * created on 2020/8/18 10:15
 *
 * @author yaozou
 * @since v1.0.0
 */
@Data
public class SplitRuleDetail {
    /**
     * 商户系统用户标识，商户系统中唯一编号。 如果是平台，则填#yunBizUserId_B2C#
     */
    private String bizUserId;
    /**
     * 如果向会员分账，不上送，默认为唯一托管账户集。
     * 如果向平台分账，请填写平台的标准账户集编号 （不支持 100003-准备金额度账户集）。
     */
    private String accountSetNo;
    /**
     * 金额，单位：分
     */
    private Long amount;
    /**
     * 手续费，内扣，单位：分
     */
    private Long fee;
    private String remark;
}
