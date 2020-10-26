package com.meiyuan.catering.order.dto.query.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情基础信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情基础信息——后台")
public class OrdersDetailBaseAdminDTO {
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("商户ID")
    private Long merchantId;
    @ApiModelProperty("门店ID")
    private Long shopId;
    @ApiModelProperty("下单时间")
    private LocalDateTime billingTime;
    @ApiModelProperty("订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private Integer orderStatus;
    @ApiModelProperty("订单服务方式。1:外卖配送，2:到店自取，3:堂食正餐，4:堂食快餐，5:堂食外带")
    private Integer deliveryWay;
    @ApiModelProperty("1:外卖小程序，2:堂食小程序")
    private Integer orderSource;
    @ApiModelProperty("下单类型。1:外卖，2:堂食")
    private Integer orderWay;
    @ApiModelProperty("支付方式（ 0:0元支付；1：余额支付；2：微信支付（WX)； 3：支付宝支付(ALP)；4：POS刷卡支付(YH)；）")
    private Integer payWay;
    @ApiModelProperty("商家名称")
    private String storeName;
    @ApiModelProperty("自提点")
    private String takeAddress;
    @ApiModelProperty("订单备注")
    private String remarks;
    @ApiModelProperty("取餐时间")
    private String actualTime;
    @ApiModelProperty("支付金额")
    private BigDecimal paidAmount;
    @ApiModelProperty("支付时间")
    private LocalDateTime paidTime;
    @ApiModelProperty("支付流水")
    private String tradingFlow;
    @ApiModelProperty("下单人姓名")
    private String userName;
    @ApiModelProperty("下单人手机号")
    private String userPhone;
    @ApiModelProperty("订单取消原因")
    private String offReason;
    @ApiModelProperty("是否退款，0：否。1：是")
    private Integer refundStatus;

    @ApiModelProperty("取消时间(订单为取消状态时展示)")
    private LocalDateTime orderCancelTime;

    @ApiModelProperty("立即送达时间[yyyy-MM-dd HH:mm]")
    private String immediateDeliveryTime;

    public Integer getPayWay() {
        return payWay==null?-1:payWay;
    }
}
