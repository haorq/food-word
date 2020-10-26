package com.meiyuan.catering.allinpay.model.param.order;

import com.alibaba.fastjson.JSONArray;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/25 16:09
 * @description 通联退款申请参数
 **/

@Data
@Builder
public class AllinPayRefundParams extends AllinPayBaseServiceParams {

    /**
     * 商户订单号（退款单号）
     *
     * 必填
     */
    private String bizOrderNo;
    /**
     * 商户原订单号
     *
     * 需要退款的原交易订单号
     *
     * 必填
     */
    private String oriBizOrderNo;
    /**
     * 商户系统用户标识，商户系统中唯一编号。退款收款人
     *
     * 必须是原订单中的付款方
     * 如果是平台，参数值为:#yunBizUserId_B2C#。
     *
     * 必填
     */
    private String bizUserId;
    /**
     * 退款方式 默认D1
     *
     * D1：D+1 14:30向渠道发起退款
     * D0：D+0实时向渠道发起退款，说明
     * （1）此参数仅对支持退款金额原路返回的支付订单有效（接口说明3）
     * （2）不支持退款的渠道及内部账户，实时退款至余额（接口说明4）
     *
     * 非必填
     */
    private String refundType;
    /**
     * 托管代收订单中的收款人的退款金额 此字段总金额=amount-feeAmount。
     *
     *（1）原订单为消费接口，不填
     *（2）原订单为简化校验版代收接口
     * A -未代付，refundList无需上送，从中间账户至原代收订单付款方
     * B-全部代付,refundList必须上送，从已代付收款人，指定托管专用账户集，上送退款列表会员bizUserId及金额amount退款至原代收订单付款方；
     * C-部分代付，分别退款，未代付金额按A规则，已代付金额按B规则退款；
     *（3）原订单为标准版托管代收接口全额退款不填，部分退款必填；
     * A-全额退款，refundList无需上送，原路径返回（未代付-从中间账户至原代收订单付款方，已代付-从已代付收款人至原代收订单付款方）；
     * B-部分退款，refundList必须上送，
     * 已代付-填写账户集编号，明确托管专用账户集，从退款列表会员bizUserId及金额amount退款至代收订单付款方，
     * 未代付-账户集不上送，默认从中间账户退至代收订单付款方；
     * 支持refundList指定既从中间账户退款，又从指定已代付收款人账户退款；
     * 注：最多支持100个
     *
     * 非必填
     */
    private JSONArray refundList;
    /**
     * 后台通知地址
     *
     * 如果不填，则不通知。退款成功时，才会通知。
     *
     * 非必填
     */
    private String backUrl;
    /**
     * 本次退款总金额 单位：分;
     *
     * 不得超过原订单金额。
     *
     * 必填
     */
    private Long amount;
    /**
     * 代金券退款金额 单位：分;
     *
     * 不得超过退款总金额，支持部分退款。
     * 如不填，则默认为0。如为0，则不退代金券。
     *
     * 非必填
     */
    private Long couponAmount;
    /**
     * 手续费退款金额 单位：分;
     *
     * 不得超过退款总金额。
     * 如不填，则默认为0。如为0，则不退手续费。
     *
     * 非必填
     */
    private Long feeAmount;
    /**
     * 扩展信息
     *
     * 接口将原样返回，最多50个字符，不可包含“|”特殊字符
     *
     * 非必填
     */
    private String extendInfo;

}
