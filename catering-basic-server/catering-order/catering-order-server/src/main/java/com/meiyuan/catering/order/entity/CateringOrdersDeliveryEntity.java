package com.meiyuan.catering.order.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 订单配送表(CateringOrdersDelivery)实体类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:14:35
 */
@Data
@TableName("catering_orders_delivery")
public class CateringOrdersDeliveryEntity extends IdEntity implements Serializable {
    private static final long serialVersionUID = 141762175167827624L;

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
    /** 取餐码 */
    private String consigneeCode;
    /** 预计送达时间 */
    private LocalDateTime estimateTime;
    /** 预计送达截止时间 */
    private LocalDateTime estimateEndTime;
    /** 立即送达时间 */
    private LocalDateTime immediateDeliveryTime;
    /** 实际取餐时间 */
    private LocalDateTime actualTime;
    /** 创建人ID */
    private Long createBy;
    /** 创建人名称 */
    private String createName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新人ID */
    private Long updateBy;
    /** 更新人名称 */
    private String updateName;
    /** 更新时间 */
    private LocalDateTime updateTime;
    /** 配送ID */
    private Long deliveryId;
}
