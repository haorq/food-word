package com.meiyuan.catering.allinpay.model.param.order;

import com.alibaba.fastjson.JSONArray;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 14:09
 * @description 通联批量托管代付列表参数
 **/

@Data
@Builder
public class AllinPayBatchPayListParams {

    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 源托管代收订单付款信息；最多支持100个；
     */
    private JSONArray collectPayList;
    /**
     * 商户系统用户标识，商户系统中唯一编号；托管代收订单中指定的收款方。
     */
    private String bizUserId;
    /**
     * 收款方的账户集编号
     */
    private String accountSetNo;
    /**
     * 后台通知地址
     */
    private String backUrl;
    /**
     * 金额，单位：分
     */
    private Long amount;
    /**
     * 手续费，单位：分
     * 内扣，如果不存在，则填0；如amount为100，fee为2，实际到账金额为98
     */
    private Long fee;
    /**
     * 内扣
     *
     * 支持分账到会员或者平台账户。
     * 分账规则：分账层级数<=3，分账总会员数<=10
     */
    private JSONArray splitRuleList;
    /**
     * 摘要；最多20个字符
     */
    private String summary;
    /**
     * 最多50个字符，商户拓展参数，用于透传给商户，不可包含“|”特殊字符
     */
    private String extendInfo;

}
