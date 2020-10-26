package com.meiyuan.catering.marketing.vo.activity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/10 14:02
 */
@Data
@ApiModel("活动效果-订单明细列表")
public class ActivityOrdersPageVO {

    @ApiModelProperty(value = "订单主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "商户ID",hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "门店ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单原价（元）")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "配送费（元）")
    private BigDecimal deliveryPrice;

    @ApiModelProperty(value = "订单应付金额（元）")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "订单实付金额（元）")
    private BigDecimal paidAmount;

    @ApiModelProperty(value = "平台成本合计（元）")
    private BigDecimal manageAmount;

    @ApiModelProperty(value = "商家成本合计（元）")
    private BigDecimal merchantCost;
}
