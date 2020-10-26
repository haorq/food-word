/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.meiyuan.catering.order.dto.submit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.entity.CateringOrdersActivityEntity;
import com.meiyuan.catering.order.entity.CateringOrdersEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单简要信息Dto
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ApiModel
public class OrderSimpleDTO implements Serializable {

    private static final long serialVersionUID = -6910867366379246264L;

    @ApiModelProperty(value = "主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "商户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "门店ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "下单客户ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long memberId;

    @ApiModelProperty(value = "下单客户编号")
    private String memberNo;

    @ApiModelProperty(value = "下单客户名称")
    private String memberName;

    @ApiModelProperty(value = "下单客户手机号")
    private String memberPhone;

    @ApiModelProperty(value = "开单时间")
    private LocalDateTime billingTime;

    @ApiModelProperty(value = "订单状态（1：代付款；2：待接单；3：待配送；4：配送中；5：待取餐；6：已完成；7：已取消）")
    private Integer status;

    @ApiModelProperty(value = "优惠前订单金额")
    private BigDecimal discountBeforeFee;

    @ApiModelProperty(value = "优惠后订单金额")
    private BigDecimal discountLaterFee;

    @ApiModelProperty(value = "支付完成时间")
    private LocalDateTime paidTime;

    @ApiModelProperty(value = "订单购买内容（JSON）")
    private String buyContent;

    @ApiModelProperty(value = "支付截止时间（订单自动关闭时间）")
    private LocalDateTime payDeadline;

    @ApiModelProperty(value = "保存成功后的订单信息", hidden = true)
    private CateringOrdersEntity orders;

    @ApiModelProperty(value = "保存成功后的订单活动信息", hidden = true)
    private List<CateringOrdersActivityEntity> activityList;

}
