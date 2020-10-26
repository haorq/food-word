package com.meiyuan.catering.order.dto.submit;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 功能描述: 订单提交配送信息DTO
 * @author: XiJie Xie
 * @date: 2020/3/10 10:45
 * @version v 1.0
 */
@Data
@TableName("catering_orders_delivery")
public class OrdersDeliveryCreateDTO implements Serializable {
    private static final long serialVersionUID = -15126619452822257L;

    private Long id;
    /** 订单ID */
    private Long orderId;
    /** 订单编号 */
    private String orderNumber;
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
    /** 预计送达、取餐时间 */
    private LocalDateTime estimateTime;
    /** 预计送达、取餐截止时间 */
    private LocalDateTime estimateEndTime;
    /** 立即送达时间 */
    private LocalDateTime immediateDeliveryTime;
    /** 取餐码 */
    private String consigneeCode;
    /** 配送ID */
    private Long deliveryId;
}
