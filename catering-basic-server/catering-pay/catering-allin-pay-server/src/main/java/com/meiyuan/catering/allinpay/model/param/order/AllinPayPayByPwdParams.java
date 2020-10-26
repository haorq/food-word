package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 09:09
 * @description 通联确认支付（前台+密码验证版）参数
 **/

@Data
@Builder
public class AllinPayPayByPwdParams extends AllinPayBaseServiceParams {

    /**
     * 商户订单号（支付订单）
     *
     * 必填
     */
    private String bizOrderNo;
    /**
     * 确认支付之后，跳转返回的页面地址
     * 如果支付方式中有网关支付，无需上传此字段
     *
     * 非必填
     */
    private String jumpUrl;
    /**
     * ip地址
     * 用户公网IP用于分控校验；
     * 注：不能使用“127.0.0.1”“localhost”
     *
     * 必填
     */
    private String consumerIp;

}
