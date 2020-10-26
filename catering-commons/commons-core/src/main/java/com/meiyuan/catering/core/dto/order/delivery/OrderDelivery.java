package com.meiyuan.catering.core.dto.order.delivery;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/5/20 11:02
 * @since v1.1.0
 */
@Data
public class OrderDelivery extends IdEntity {

    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 自提点表ID（店铺表id）
     */
    private Long storeId;
    /**
     * 自提点名称
     */
    private String storeName;
    /**
     * 自提点负者人
     */
    private String storeManager;
    /**
     * 自提点电话
     */
    private String storePhone;
    /**
     * 取餐人姓名
     */
    private String consigneeName;
    /**
     * 取餐人性别（1：男；2-女；3-其他）
     */
    private Integer consigneeSex;
    /**
     * 取餐人电话
     */
    private String consigneePhone;
    /**
     * 餐具数量（单位：份）
     */
    private Integer tableware;
    /**
     * 收货地区
     */
    private String consigneeArea;
    /**
     * 详细地址
     */
    private String consigneeAddress;
    /**
     * 取餐码
     */
    private String consigneeCode;
    /**
     * 预计送达时间
     */
    private LocalDateTime estimateTime;
    /**
     * 预计送达截止时间
     */
    private LocalDateTime estimateEndTime;

    /**
     * 立即送达时间
     */
    private LocalDateTime immediateDeliveryTime;
    /**
     * 实际取餐时间
     */
    private LocalDateTime actualTime;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     *收货地址表id
     */
    private Long deliveryId;
}
