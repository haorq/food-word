package com.meiyuan.catering.allinpay.model.param.order;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.enums.service.DeviceTypeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.IndustryEnums;
import com.meiyuan.catering.allinpay.enums.service.order.TradeCodeEnums;
import com.meiyuan.catering.allinpay.enums.service.order.ValidateTypeEnums;
import com.meiyuan.catering.allinpay.model.bean.order.AllinReciever;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/09/25 09:09
 * @description 通联托管代收申请参数
 **/


@Data
@Builder
public class AllinPayAgentCollectApplyParams extends AllinPayBaseServiceParams {

    /**
     * 商户订单号（支付订单）
     * <p>
     * 必填
     */
    private String bizOrderNo;
    /**
     * 商户系统用户标识，商户系统中唯一编号。
     * 付款人
     * 付款用户的 bizUserId，支持个人会 员、企业会员的
     * <p>
     * 必填
     */
    private String payerId;

    /**
     * 收款列表
     * 最多支持2000个
     * <p>
     * 必填
     */
    private List<AllinReciever> recieverList;

    /**
     * 业务码
     * <p>
     * {@link TradeCodeEnums}
     * <p>
     * 必填
     */
    private String tradeCode;
    /**
     * 订单金额 单位：分。
     * 订单金额=收款列表总金额+手续费
     * <p>
     * 必填
     */
    private Integer amount;
    /**
     * 手续费内扣，如果不存在，则填 0。 单位：分。
     * 如 amount为 100，fee 为 2，实际到账金额为 98。
     * <p>
     * 必填
     */
    private Integer fee;

    /**
     * 交易验证方式
     * 如不填，默认为短信验证码确认。
     * 平台提现，只支持无验证和短线验证
     * <p>
     * {@link ValidateTypeEnums}
     * <p>
     * 非必填
     */
    private Long validateType;
    /**
     * 前台通知地址
     * 前台交易时必填，支付后，跳转的 前台页面
     * 小通网关模式必传
     * 收银宝网关必传
     * 收银宝 h5 收银台必传
     * 通联钱包 H5 必传
     * <p>
     * 非必填
     */
    private String frontUrl;

    /**
     * 后台通知地址
     * <p>
     * 必填
     */
    private String backUrl;
    /**
     * 订单过期时间
     * yyyy-MM-dd HH:mm:ss 控制订单可支付时间，订单最长时 效为 24 小时，即过期时间不能大于 订单创建时间 24 小时；
     * 1） 需确认支付情况-确认支付时 间不能大于订单过期时间； 注：收银宝 H5 收银台若未上送订 单过期时间，确认支付后默认订单 有效期 5 分钟
     * 2） 无需确认支付情况-透传至渠 道方，最大不超过 60 分钟，控 制订单支付时间范围，下述支 付方式有效：
     * 微信正扫： SCAN_WEIXIN、 SCAN_WEIXIN_ORG
     * 支付宝正扫： SCAN_ALIPAY、 SCAN_ALIPAY_ORG
     * 银联正扫： SCAN_UNIONPAY、 SCAN_UNIONPAY_ORG
     * 微信公众号： WECHAT_PUBLIC、 WECHAT_PUBLIC_ORG
     * 微信小程序： WECHATPAY_MINIPROGRAM、 WECHATPAY_MINIPROGRAM_ORG
     * 微信原生 APP： WECHATPAY_APP_OPEN
     * <p>
     * 非必填
     */
    private String orderExpireDatetime;

    /**
     * 支付方式 包：paymethod
     * 支付金额等于订单金额
     * <p>
     * 必填
     */
    private JSONObject payMethod;

    /**
     * 行业代码
     * <p>
     * {@link IndustryEnums}
     * <p>
     * 必填
     */
    private String industryCode;
    /**
     * 行业名称
     * <p>
     * 必填
     */
    private String industryName;
    /**
     * 访问终端类型
     * <p>
     * {@link DeviceTypeEnums}
     * <p>
     * 必填
     */
    private Long source;
    /**
     * 摘要最多 20 个字符
     * <p>
     * 非必填
     */
    private String summary;
    /**
     * 扩展信息
     * 最多50个字符，商户拓展参数，用于透传给商户，不可包含“|”特殊字符
     * <p>
     * 非必填
     */
    private String extendInfo;

}
