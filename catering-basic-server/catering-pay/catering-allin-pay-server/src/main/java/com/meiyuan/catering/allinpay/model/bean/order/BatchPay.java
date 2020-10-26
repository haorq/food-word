package com.meiyuan.catering.allinpay.model.bean.order;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/10/9 9:47
 * @since v1.5.0
 */
@Data
@Builder
public class BatchPay {
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 源托管代收订单付款信息
     */
    private List<CollectPay> collectPayList;
    /**
     * 支持个人会员、企业会员、平台。 若平台，上送固定值： #yunBizUserId_B2C#
     */
    private String bizUserId;
    /**
     * 收款人的账户集编号。
     */
    private String accountSetNo;
    /**
     * 后台通知地址
     */
    private String backUrl;
    /**
     * 总金额，单位：分
     */
    private Long amount;
    /**
     * 手续费
     * 内扣，如果不存在，则填 0 单位：分。
     * 如 amount 为 100，fee 为 2，实际 到账金额为 98。
     */
    private Long fee;
    /**
     * 分账规则
     * 内扣。
     * 支持分账到会员或者平台账户。
     * 分账规则：分账层级数<=2，分账总会员数<=10
     */
    private List<SplitRuleDetail> splitRuleList;
    /**
     * 业务码
     */
    private String tradeCode;
    /**
     * 交易内容摘要。 最多 20 个字符
     */
    private String summary;
    /**
     * 最多 50 个字符，商户拓展参数，用于透传给商户，不可包 含“|”特殊字符
     */
    private String extendInfo;
}
