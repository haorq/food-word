package com.meiyuan.catering.order.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MeiTao
 * @create ${cfg.dateTime}
 * @describe 实体类
 */
@TableName("catering_orders_delivery_status_history")
@ApiModel(value="CateringOrderDeliveryStatusHistoryEntity对象", description="")
@Data
public class CateringOrderDeliveryStatusHistoryEntity extends IdEntity implements Serializable {

        @ApiModelProperty(value = "业务系统订单ID")
        @TableField("order_id")
        private Long orderId;

        @ApiModelProperty(value = "返回达达运单号，默认为空")
        @TableField("client_id")
        private String clientId;

        @ApiModelProperty(value = "配送状态")
        @TableField("order_status")
        private Integer orderStatus;

        @ApiModelProperty(value = "订单取消原因,其他状态下默认值为空字符串")
        @TableField("cancel_reason")
        private String cancelReason;

        @ApiModelProperty(value = "订单取消原因来源(1:达达配送员取消；2:商家主动取消；3:系统或客服取消；0:默认值)")
        @TableField("cancel_from")
        private Integer cancelFrom;

        @ApiModelProperty(value = "更新时间,时间戳")
        @TableField("update_time")
        private Date updateTime;

        @ApiModelProperty(value = "达达配送员id，接单以后会传")
        @TableField("dm_id")
        private Integer dmId;

        @ApiModelProperty(value = "配送员姓名，接单以后会传")
        @TableField("dm_name")
        private String dmName;

        @ApiModelProperty(value = "配送员手机号，接单以后会传")
        @TableField("dm_mobile")
        private String dmMobile;

        @ApiModelProperty(value = "对client_id, order_id, update_time的值进行字符串升序排列，再连接字符串，取md5值")
        @TableField("signature")
        private String signature;

        @TableField("create_time")
        private Date createTime;
}







