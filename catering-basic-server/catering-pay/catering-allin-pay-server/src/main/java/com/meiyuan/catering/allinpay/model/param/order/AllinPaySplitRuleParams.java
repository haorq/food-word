package com.meiyuan.catering.allinpay.model.param.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/24 18:09
 * @description 通联支付分账规则  说明：1.支持向平台用户、平台分账。 2.支持多级分账、多角色分账。
 *                               分账规则：分账层级数<=3，分账总会员数<=10
 **/

@Data
@Builder
public class AllinPaySplitRuleParams {

    /**
     * 商户系统用户标识，商户系统中唯一编号。如果是平台，则填#yunBizUserId_B2C#
     *
     * 必填
     */
    private String bizUserId;
    /**
     * 如果向会员分账，不上送，默认为唯一托管账户集。如果向平台分账，请填写平台的标准账户集编号（不支持100003-准备金额度账户集）
     *
     * 非必填
     */
    private String accountSetNo;
    /**
     * 分账金额，单位：分
     *
     * 必填
     */
    private Long amount;
    /**
     * 分账手续费，内扣，单位：分
     * 0 - 没有手续费
     *
     * 必填
     */
    private Long fee;
    /**
     * 备注，最长50个字符
     *
     * 非必填
     */
    private String remark;
    /**
     * 分账列表
     */
    private List<AllinPaySplitRuleParams> splitRuleList;

}
