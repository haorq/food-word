package com.meiyuan.catering.allinpay.model.param.order;

import lombok.Builder;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/27 10:09
 * @description 通联支付源托管代收订单付款信息参数
 **/

@Data
@Builder
public class AllinPayCollectPayListParams {

    /**
     * 相关代收交易的“商户订单号”
     */
    private String bizOrderNo;
    /**
     * 金额，单位：分；
     * 部分代付时，可以少于或等于托管代收订单金额
     */
    private Long amount;

}
