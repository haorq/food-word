package com.meiyuan.catering.allinpay.model.param.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/24 18:09
 * @description 通联消费参数
 **/

@Data
@Builder
public class AllinPayConsumeApplyParams {

    /**
     * 商户系统用户标识，商户系统中唯一编号。付款方
     * 消费用户的bizUserId，支持个人会员、企业会员
     *
     * 必填
     */
    private String payerId;
    /**
     * 商户系统用户标识，商户系统中唯一编号。收款方
     * 消费商户的bizUserId，支持个人会员、企业会员、平台。如果是平台，参数值为：#yunBizUserId_B2C#
     *
     * 必填
     */
    private String recieverId;
    /**
     * 商户订单号（支付订单）
     *
     * 必填
     */
    private String bizOrderNo;
    /**
     * 订单金额 单位：分。
     *
     * 必填
     */
    private Long amount;
    /**
     * 手续费 单位：分。
     * 内扣，如果不存在，则填0。
     * 如amount为100，fee为2，则充值实际到账为98，平台手续费收入为2。
     *
     * 必填
     */
    private Long fee;
    /**
     * 交易验证方式
     *  0 - 无验证 仅渠道验证，通商云不做交易验证
     *  1 - 短信验证码 通商云发送并验证短信验证码，有效期3分钟。
     *  2 - 支付密码 验证通商云支付密码
     *
     *  如不填，默认为短信验证码确认。
     *  平台提现，只支持无验证和短线验证
     *
     *  非必填
     */
    private Long validateType;
    /**
     * 分账规则，内扣。
     * 支持分账到会员或者平台账户。
     * 分账规则：分账层级数<=3，分账总会员数<=10
     *
     * 非必填
     */
    private JSONArray splitRule;
    /**
     * 前台通知地址
     * 前台交易时必填，支付后，跳转的前台页面；
     * 小通网关模式必传；收银宝网关必传
     *
     * 非必填
     */
    private String frontUrl;
    /**
     * 后台通知地址
     *
     * 必填
     */
    private String backUrl;
    /**
     * 订单过期时间
     * 格式：yyyy-MM-dd HH:mm:ss
     * 控制订单可支付时间，订单最长时效为24小时，即过期时间不能大于订单创建时间24小时；
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
     * 商品类型
     *  2 - 虚拟商品
     *  3 - 实物商品
     *  4 - 线下服务
     *  5 - 跨境商品
     *  90 - 营销活动
     *  99 - 其他
     *
     *  非必填
     */
    private Long goodsType;
    /**
     * 商户系统商品编号
     * 商家录入商品后，发起交易时可上送商品编号
     *
     * 非必填
     */
    private String bizGoodsNo;
    /**
     * 商品名称  注：符号不支持
     * 微信原生APP支付必传
     * 收银宝渠道：最大100个字节(50个中文字符)
     *
     * 非必填
     */
    private String goodsName;
    /**
     * 商品描述
     *
     * 非必填
     */
    private String goodsDesc;
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
     *  1 - Mobile
     *  2 - PC
     *
     * 必填
     */
    private Long source;
    /**
     * 摘要
     * 交易内容最多20个字符
     *
     * 非必填
     */
    private String summary;
    /**
     * 扩展信息
     * 最多50个字符，商户拓展参数，用于透传给商户，不可包含“|”特殊字符
     *
     * 非必填
     */
    private String extendInfo;

}
