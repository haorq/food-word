package com.meiyuan.catering.order.vo;

import lombok.Data;

/**
 * @author yaoozu
 * @description 订单基本信息
 * @date 2020/4/13 14:44
 * @since v1.0.0
 */
@Data
public class OrderBaseVo {
    /** 商户ID */
    private Long merchantId;
    /** 订单ID */
    private Long orderId;
    /** 订单号 */
    private String orderNumber;
    /** 服务方式 **/
    private Integer deliveryWay;
}
