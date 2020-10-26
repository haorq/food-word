package com.meiyuan.catering.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Author MeiTao
 * @Date 2020/10/12 0012 11:16
 * @Description 简单描述 :
 * @Since version-1.5.0
 */
@TableName("catering_orders_delivery_cancel_record")
@ApiModel(value="CateringOrdersDeliveryCancelRecordEntity对象", description="骑手主动取消订单，推送消息记录表")
@Data
public class CateringOrdersDeliveryCancelRecordEntity extends IdEntity implements Serializable {

        @ApiModelProperty(value = "业务订单ID")
        @TableField("order_id")
        private Long orderId;

        @ApiModelProperty(value = "达达订单号，达达方非必传")
        @TableField("dada_order_id")
        private Long dadaOrderId;

        @ApiModelProperty(value = "骑士取消原因")
        @TableField("cancel_reason")
        private String cancelReason;

        @ApiModelProperty(value = "商家处理结果。1:同意。2:不同意 默认：0")
        @TableField("deal_ret")
        private Integer dealRet;

        @TableField("create_time")
        private Date createTime;
}







