package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/29 14:09
 * @description 通联确认支付（前台+短信验证码确认）参数
 **/

@Data
public class AllinPayPayBySmsParams extends AllinPayBaseServiceParams {

    /**
     * 商户系统用户标识，商户系统中唯一编号。
     *
     * 支持个人会员、企业会员、平台。
     * 若平台，上送固定值：#yunBizUserId_B2C#
     */
    private String bizUserId;
    /**
     * 商户订单号（支付订单）
     */
    private String bizOrderNo;
    /**
     * 短信验证码
     */
    private String verificationCode;
    /**
     * ip地址
     *
     * 用户公网IP用于分控校验；
     * 注：不能使用“127.0.0.1”“localhost”
     */
    private String consumerIp;

}
