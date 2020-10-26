package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/29 14:09
 * @description 通联确认支付（后台+短信验证码确认）参数
 **/

@Data
public class AllinPayPayByBackSmsParams extends AllinPayBaseServiceParams {

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 支持个人会员、企业会员、平台。
     * 若平台，上送固定值：#yunBizUserId_B2C#
     *
     * 必填
     */
    private String bizUserId;
    /**
     * 商户订单号（支付订单）
     *
     * 必填
     */
    private String bizOrderNo;
    /**
     * 交易编号
     *
     * 必填
     */
    private String tradeNo;
    /**
     * 短信验证码
     *
     * 必填
     */
    private String verificationCode;
    /**
     * ip地址
     *
     * 用户公网IP用于分控校验；
     * 注：不能使用“127.0.0.1”“localhost”
     *
     * 必填
     */
    private String consumerIp;

}
