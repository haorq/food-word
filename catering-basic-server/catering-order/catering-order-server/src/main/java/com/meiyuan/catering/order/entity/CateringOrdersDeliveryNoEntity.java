package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author MeiTao
 * @create ${cfg.dateTime}
 * @describe 订单与第三方配送单号关联表实体类
 */
@TableName("catering_orders_delivery_no")
@ApiModel(value = "CateringOrdersDeliveryNoEntity对象", description = "订单与第三方配送单号关联表")
@Data
public class CateringOrdersDeliveryNoEntity extends IdEntity implements Serializable {

    @ApiModelProperty(value = "业务系统订单ID")
    @TableField("order_id")
    private Long orderId;

    @ApiModelProperty(value = "第三方配送系统订单编号")
    @TableField("delivery_no")
    private String deliveryNo;

    @ApiModelProperty(value = "第三方配送服务商")
    @TableField("delivery_type")
    private String deliveryType;

    @ApiModelProperty(value = "第三方配送服务商描述")
    @TableField("delivery_type_remark")
    private String deliveryTypeRemark;
    @ApiModelProperty(value = "距离（单位：米）")
    @TableField("distance")
    private BigDecimal distance;
    @ApiModelProperty(value = "实际运费(单位：元)，运费减去优惠券费用")
    @TableField("fee")
    private BigDecimal fee;
    @ApiModelProperty(value = "运费（单位：元）")
    @TableField("delivery_fee")
    private BigDecimal delivery_fee;
    @ApiModelProperty(value = "优惠券费用(单位：元)")
    @TableField("coupon_fee")
    private BigDecimal coupon_fee;
    @ApiModelProperty(value = "商户取消订单，达达扣除违约金")
    @TableField("deduct_fee")
    private BigDecimal deduct_fee;

    @TableField("create_time")
    private Date createTime;
}







