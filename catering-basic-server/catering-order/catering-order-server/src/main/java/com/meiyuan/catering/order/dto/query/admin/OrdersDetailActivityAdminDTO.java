package com.meiyuan.catering.order.dto.query.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单详情活动信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情活动信息——后台")
public class OrdersDetailActivityAdminDTO implements Serializable {
    @ApiModelProperty("订单活动Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long activityId;
    @ApiModelProperty("名称")
    private String discountName;
    @ApiModelProperty("优惠类型：1：满减卷")
    private Integer discountType;
    @ApiModelProperty(value = "使用条件：1：订单优惠；2：商品优惠")
    private Integer usefulCondition;
    @ApiModelProperty("描述")
    private String describe;
    @ApiModelProperty("数量")
    private Integer number;
    @ApiModelProperty("优惠金额")
    private BigDecimal activityDiscount;
    @ApiModelProperty("优惠券面额")
    private BigDecimal ticketAmount;
    @ApiModelProperty("优惠券所属")
    private Integer sendTicketParty;
    @ApiModelProperty("优惠券所属（解析后的中文描述，直接展示即可）")
    private String sendTicketPartyDesc;
    @ApiModelProperty("优惠券使用门槛")
    private BigDecimal consumeCondition;
    @ApiModelProperty("活动名称")
    private String activityName;
}
