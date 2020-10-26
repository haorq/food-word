package com.meiyuan.catering.order.dto.query.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.dto.order.OrderDeliveryStatusDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 订单详情信息——商户
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情信息——商户")
public class OrdersDetailMerchantDTO {
    @ApiModelProperty("订单ID")
    private Long orderId;
    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("下单时间")
    private LocalDateTime billingTime;
    @ApiModelProperty("取餐人姓名")
    private String consigneeName;
    @ApiModelProperty("取餐人电话")
    private String consigneePhone;
    @ApiModelProperty("预计送达时间")
    private Date estimateTimeWithTime;
    @ApiModelProperty("预计送达时间")
    private String estimateTime;
    @ApiModelProperty("预计送达截止时间")
    private String estimateEndTime;
    @ApiModelProperty("预计送达日期")
    private String estimateDate;
    @ApiModelProperty("立即送达时间[HH:mm]")
    private String immediateDeliveryTime;
    @ApiModelProperty("立即送达时间【yyyy-MM-dd HH:mm:ss】")
    private LocalDateTime immediateDeliveryDate;
    @ApiModelProperty("实际取餐时间")
    private String actualTime;
    @ApiModelProperty("送达日期")
    private String deliveryDate;
    @ApiModelProperty("收货地址")
    private String consigneeAddress;
    @ApiModelProperty("收货地址经纬度(经度，纬度)")
    private String mapCoordinate;
    @ApiModelProperty("门店经纬度(经度，纬度)")
    private String shopMapCoordinate;
    @ApiModelProperty(value = "数据库中订单状态【内部使用】（1：代付款；2：待接单；3：待配送；4：待取餐；5：已完成；6：已取消；7：已关闭；8：团购中）", hidden = true)
    private Integer orderStatus;
    @ApiModelProperty("app订单状态（1：待配送；2：待自取；3：已完成；4：已取消；5：已失效；6：待退款；7：已退款）")
    private Integer appOrderStatus;
    @ApiModelProperty("订单状态描述（1：待配送；2：待自取；3：已完成；4：已取消；5：已失效；6：待退款；7：已退款）")
    private String orderStatusStr;
    @ApiModelProperty("取餐方式（1：外卖配送，2：到店自取）")
    private Integer deliveryWay;
    @ApiModelProperty("取餐方式描述（1：外卖配送，2：到店自取）")
    private String deliveryWayStr;
    @ApiModelProperty("商品总金额")
    private BigDecimal goodsAmount;
    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;
    @ApiModelProperty("订单优惠金额")
    private BigDecimal discountFee;
    @ApiModelProperty("配送费")
    private BigDecimal deliveryPrice;
    @ApiModelProperty("配送费原始价格")
    private BigDecimal deliveryPriceOriginal;
    /**
     * @version 1.5.0
     * @author lh
     * @desc 餐盒费
     */
    @ApiModelProperty("餐盒费")
    private BigDecimal packPrice;
    @ApiModelProperty("订单归属（自提点）")
    private String takeAddress;
    @ApiModelProperty("订单备注")
    private String remarks;
    @ApiModelProperty("餐具数量（单位：份）")
    private Integer tableware;
    @ApiModelProperty("是否申请售后【内部使用】（0：否[默认]； 1：是）")
    private Boolean afterSales;
    @ApiModelProperty("退款单id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long refundId;
    @ApiModelProperty("退款状态")
    private Integer refundStatus;
    @ApiModelProperty("退款原因")
    private String refundReason;
    @ApiModelProperty(value = "退款原因字典明细", hidden = true)
    private List<Integer> refundReasonList;
    @ApiModelProperty("退款说明")
    private String refundRemark;
    @ApiModelProperty("退款编号")
    private String refundNumber;
    @ApiModelProperty("退款凭证(图片地址:最多3张)")
    private List<String> refundEvidence;
    @ApiModelProperty("货物状态 1:已收到货 2:未收到货")
    private Integer cargoStatus;
    @ApiModelProperty("货物状态描述")
    private String cargoStatusStr;

    @ApiModelProperty("订单操作说明如：订单取消原因")
    private String operationExplain;

    @ApiModelProperty("订单商品数量")
    private Integer orderGoodsSize;
    @ApiModelProperty("订单商品信息")
    private List<OrdersGoodsMerchantDTO> ordersGoods;
    @ApiModelProperty("订单赠送商品信息")
    private List<OrdersGiftGoodsMerchantDTO> ordersGiftGoods;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("门店所在城市中文名")
    private String shopCity;
    @ApiModelProperty("配送类型，1：自配送。2：达达")
    private Integer deliveryType;
    @ApiModelProperty("是否需要下发达达，0：不需要。1：需要")
    private Integer shopDeliveryFlag;
    @ApiModelProperty("门店ID")
    private Long shopId;
    @ApiModelProperty("品牌ID")
    private Long merchantId;
    @ApiModelProperty("第三方配送状态")
    private List<OrderDeliveryStatusDto> orderDeliveryStatusList;

    @ApiModelProperty("是否补打小票：true：是")
    private Boolean reprint;
}
