package com.meiyuan.catering.order.dto.query.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 充值消费列表信息——后台端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("充值消费列表信息——后台端")
public class TopUpConsumeListAdminDTO {
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("消费时间")
    private LocalDateTime paidTime;
    @ApiModelProperty("优惠名称")
    private List<String> discountName;
    @ApiModelProperty("优惠前订单金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountFee;
    @ApiModelProperty("优惠后订单金额")
    private BigDecimal discountLaterFee;
    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;
    @ApiModelProperty("实收金额")
    private BigDecimal paidAmount;
    @ApiModelProperty("扣减账户")
    private String account;
    @ApiModelProperty("商品信息")
    List<OrdersListGoodsAdminDTO> goodsInfo;
}
