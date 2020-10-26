package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.bean.order.CollectPay;
import com.meiyuan.catering.allinpay.model.bean.order.SplitRuleDetail;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/27 10:09
 * @description 通联支付单笔托管代付（标准版）参数
 **/

@Data
@Builder
public class AllinPaySignalAgentPayParams extends AllinPayBaseServiceParams {

    /**
     * 商户订单号（支付订单）
     * <p>
     * 必填
     */
    private String bizOrderNo;
    /**
     * 源托管代收订单付款信息
     * 最多支持100个
     * <p>
     * 必填
     */
    private List<CollectPay> collectPayList;
    /**
     * 商户系统用户标识，商户系统中唯一编号。
     * 收款账户bizUserId，支持个人会员、企业会员
     * <p>
     * 必填
     */
    private String bizUserId;
    /**
     * 收款人的账户集编号。
     * 通商云分配的托管专用账户集的编号
     * <p>
     * 必填
     */
    private String accountSetNo;
    /**
     * 后台通知地址
     * <p>
     * 必填
     */
    private String backUrl;
    /**
     * 订单金额	单位：分。
     * <p>
     * 必填
     */
    private Long amount;
    /**
     * 手续费 单位：分;
     * 内扣，如果不存在，则填0;
     * 如amount为100，fee为2，则充值实际到账为98，平台手续费收入为2。
     * <p>
     * 必填
     */
    private Long fee;
    /**
     * 分账规则	内扣。
     * 支持分账到会员或者平台账户。
     * 分账规则：分账层级数<=3，分账总会员数<=10
     * <p>
     * 非必填
     */
    private List<SplitRuleDetail> splitRule;
    /**
     * 商品类型
     * <p>
     * 非必填
     */
    private Long goodsType;
    /**
     * 商户系统商品编号
     * 商家录入商品后，发起交易时可上送商品编号
     * <p>
     * 非必填
     */
    private String bizGoodsNo;
    /**
     * 业务码
     * <p>
     * 必填
     */
    private String tradeCode;
    /**
     * 摘要	交易内容最多20个字符
     * <p>
     * 非必填
     */
    private String summary;
    /**
     * 扩展信息
     * <p>
     * 最多50个字符，商户拓展参数，用于透传给商户，不可包含“\
     * <p>
     * 非必填
     */
    private String extendInfo;

}
