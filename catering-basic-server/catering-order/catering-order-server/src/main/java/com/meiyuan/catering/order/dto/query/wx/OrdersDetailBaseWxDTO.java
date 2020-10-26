package com.meiyuan.catering.order.dto.query.wx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情基础信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情基础信息——后台")
public class OrdersDetailBaseWxDTO {
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("系统交易流水编号(商户交易单号)")
    private String tradingFlow;
    @ApiModelProperty("下单时间")
    private LocalDateTime billingTime;
    @ApiModelProperty("支付截止时间")
    private LocalDateTime payDeadline;
    @ApiModelProperty("下单店铺Id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long storeId;
    @ApiModelProperty("下单店铺名称")
    private String storeName;
    @ApiModelProperty("下单店铺地址")
    private String addressFull;
    @ApiModelProperty("下单店铺经纬度")
    private String mapCoordinate;
    @ApiModelProperty("门店图片")
    private String storePicture;
    @ApiModelProperty("商家电话")
    private String merchantPhone;
    @ApiModelProperty(value = "订单类型：1：普通订单，2：团购订单，3：拼单订单，4：菜单订单")
    private Integer orderType;
    @ApiModelProperty("订单状态（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）")
    private Integer orderStatus;
    @ApiModelProperty("用户属性（1：企业用户；2：个人用户）")
    private Integer memberType;
    @ApiModelProperty("取餐方式（1：外卖配送，2：到店自取）")
    private Integer deliveryWay;
    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    /**
     * @version 1.5.0
     * @author lh
     * @desc 单个商品的餐盒费
     */
    @ApiModelProperty("打包费")
    private BigDecimal packPrice;
    @ApiModelProperty("配送费初始价格")
    private BigDecimal deliveryPriceOriginal;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountFee;
    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;
    @ApiModelProperty("已支付金额")
    private BigDecimal paidAmount;
    @ApiModelProperty(value = "订单关闭/取消原因")
    private String offReason;
    @ApiModelProperty("是否已评论（false：否；true ：是）")
    private Boolean comment;
    @ApiModelProperty("订单备注")
    private String remarks;
    @ApiModelProperty("餐具数量（单位：份）")
    private Integer tableware;
    @ApiModelProperty("是否可以申请售后（0：不能[默认]； 1：能）")
    private Boolean canAfterSales;
}
