package com.meiyuan.catering.allinpay.model.param.order;

import com.meiyuan.catering.allinpay.model.param.AllinPayBaseServiceParams;
import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/25 18:09
 * @description 订单信息查询参数
 **/

@Data
@Builder
public class AllinPayOrderDetailParams extends AllinPayBaseServiceParams {

    /**
     * 商户订单号（支付订单）
     *
     * 必填
     */
    private String bizOrderNo;

}
