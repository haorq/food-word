package com.meiyuan.catering.order.dto.calculate;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单配送信息
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@ToString(callSuper = true)
public class OrdersCalculateDeliveryDTO implements Serializable {
    private static final long serialVersionUID = 141762175167827624L;

    /** 自提点表ID（店铺表id） */
    private Long storeId;
    /** 自提点名称 */
    private String storeName;
    /** 自提点负者人 */
    private String storeManager;
    /** 自提点电话 */
    private String storePhone;
    /** 取餐人姓名 */
    private String consigneeName;
    /** 取餐人性别（1：男；2-女；3-其他） */
    private Integer consigneeSex;
    /** 取餐人电话 */
    private String consigneePhone;
    /** 餐具数量（单位：份） */
    private Integer tableware;
    /** 收货地区 */
    private String consigneeArea;
    /** 详细地址 */
    private String consigneeAddress;
    /** 预计送达时间 */
    private LocalDateTime estimateTime;
    /** 预计送达截止时间 */
    private LocalDateTime estimateEndTime;
    /** 立即送达时间 */
    private LocalDateTime immediateDeliveryTime;

}
