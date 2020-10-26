package com.meiyuan.catering.allinpay.model.param.order;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/28 15:09
 * @description 通联充值申请参数
 **/

@Data
public class AllinPayDepositApplyParams extends AllinPayBaseServiceParams {

    /**
     * 商户订单号（支付订单）
     *
     * 必填
     */
    private String bizOrderNo;
    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 支持个人会员、企业会员、平台。若平台，上送固定值：#yunBizUserId_B2C#
     *
     * 必填
     */
    private String bizUserId;
    /**
     * 账户集编号
     *
     * 个人会员、企业会员填写托管专用账户集编号若平台，填写平台对应账户集编号
     * 注：不支持100004/5中间账户集
     *
     * 必填
     */
    private String accountSetNo;
    /**
     * 订单金额	单位：分。
     *
     * 包含手续费
     *
     * 必填
     */
    private Long amount;
    /**
     * 手续费
     *
     * 内扣，如果不存在，则填0。单位：分。
     * 如amount为100，fee为2，则充值实际到账为98，平台手续费收入为2。
     *
     * 必填
     */
    private Long fee;
    /**
     * 交易验证方式
     *
     * 如不填，默认为短信验证码确认
     *
     * 非必填
     */
    private Long validateType;
    /**
     * 前台通知地址
     *
     * 前台交易时必填，支付后，跳转的前台页面；小通网关模式必传收银宝网关必传
     */
    private String frontUrl;
    /**
     * 后台通知地址
     *
     * 必填
     */
    private String backUrl;
    /**
     * 订单过期时间	yyyy-MM-dd HH:mm:ss控制订单可支付时间，订单最长时效为24小时，即过期时间不能大于订单创建时间24小时；
     * 1）需确认支付情况-确认支付时间不能大于订单过期时间；
     * 2）无需确认支付情况-透传至渠道方，最大不超过60分钟，控制订单支付时间范围，下述支付方式有效：
     *   微信正扫： SCAN_WEIXIN、SCAN_WEIXIN_ORG
     *   支付宝正扫： SCAN_ALIPAY、SCAN_ALIPAY_ORG
     *   银联正扫： SCAN_UNIONPAY、SCAN_UNIONPAY_ORG
     *   微信公众号：WECHAT_PUBLIC、WECHAT_PUBLIC_ORG
     *   微信小程序：WECHATPAY_MINIPROGRAM、WECHATPAY_MINIPROGRAM_ORG
     *   微信原生APP：WECHATPAY_APP_OPEN
     *
     * 非必填
     */
    private String orderExpireDatetime;
    /**
     * 支付方式
     *
     * 必填
     */
    private JSONObject payMethod;
    /**
     * 商品名称
     * 注：符号不支持，微信原生APP支付必传
     *
     * 非必填
     */
    private String goodsName;
    /**
     * 行业代码
     *
     * 必填
     */
    private String industryCode;
    /**
     * 行业名称
     *
     * 必填
     */
    private String industryName;
    /**
     * 访问终端类型
     *
     * 必填
     */
    private Long source;
    /**
     * 摘要
     *
     * 交易内容最多20个字符
     *
     * 非必填
     */
    private String summary;
    /**
     * 扩展信息
     *
     * 最多50个字符，商户拓展参数，用于透传给商户，不可包含“|”特殊字符
     *
     * 非必填
     */
    private String extendInfo;

}
