package com.meiyuan.catering.allinpay.model.result.order;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/25 09:09
 * @description 通联托管代收申请返回参数
 **/

@Data
public class AllinPayAgentCollectApplyResult {

    /**
     * 支付状态
     *
     * 仅交易验证方式为“0”时返回
     * 成功：success
     * 进行中：pending
     * 失败：fail
     * 订单成功时会发订单结果通知商户。
     */
    private String payStatus;
    /**
     * 支付失败信息
     *
     * 仅交易验证方式为“0”时返回
     * payStatus为fail时有效
     */
    private String payFailMessage;
    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 仅交易验证方式为“0”时返回
     * 平台，返回:#yunBizUserId_B2C#
     */
    private String bizUserId;
    /**
     * 通商云订单号
     */
    private String orderNo;
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 请求渠道流水号
     *
     * 针对收银宝手机银行APP支付才返回
     * 商户取此字段值作为收银宝reqsn订单号上送给收银宝；
     */
    private String reqPayInterfaceNo;
    /**
     * 交易编号
     */
    private String tradeNo;
    /**
     * POS支付的付款码
     *
     * 6位数字，收银宝ORDER_VSPPAY支付时必传
     */
    private String payCode;
    /**
     * 扩展信息
     *
     * 	商户拓展参数，用于透传给商户，不可包含“|”特殊字符
     */
    private String extendInfo;
    /**
     * 微信APP支付信息
     *
     * 微信app支付必传（收银宝及原生）
     */
    private JSONObject weChatAPPInfo;
    /**
     * 扫码支付信息/ JS支付串信息（微信、支付宝、QQ钱包）/微信小程序/微信原生H5支付串信息/支付宝原生APP支付串信息
     *
     * 1、扫码支付(正扫)必传;微信、支付宝的支付串，供转化为二维码
     * 2. JS支付必传;微信公众号JS支付：返回json字符串。支付宝JS支付：返回json字符串。支付宝服务窗返回JSAPI支付：接口参数tradeNO。QQ钱包的的公众号JS支付：返回支付的链接,消费者只需跳转到此链接即可完成支付。
     * 3、微信小程序支付参数注：有效时间60分钟4、微信原生H5支付参数微信返回mweb_url支付跳转链接,消费者只需跳转此链接完成支付，有效期5分钟5、支付宝原生APP支付参数，商户获取支付串需解密一次，方法URLDecoder.decode(str, "UTF8");
     */
    private String payInfo;
    /**
     * 交易验证方式
     *
     * 当支付方式为收银宝快捷且需验证短信验证码时才返回
     * 返回值为“1”表示需继续调用【确认支付（后台+短信验证码确认）】
     */
    private Long validateType;


}
