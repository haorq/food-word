package com.meiyuan.catering.order.dto.query.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.order.dto.query.OrdersListBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单列表信息——商户端
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单列表信息——商户端")
public class OrdersListMerchantDTO extends OrdersListBaseDTO {

    @ApiModelProperty("取餐人姓名")
    private String consigneeName;
    @ApiModelProperty("取餐人电话")
    private String consigneePhone;
    @ApiModelProperty("预计送达时间")
    private String estimateTime;
    @ApiModelProperty("预计送达截止时间")
    private String estimateEndTime;
    @ApiModelProperty("立即送达时间【HH:mm】")
    private String immediateDeliveryTime;
    @ApiModelProperty("立即送达时间【yyyy-MM-dd HH:mm:ss】")
    private LocalDateTime immediateDeliveryDate;
    @ApiModelProperty("预计送达日期")
    private String estimateDate;
    @ApiModelProperty("收货地址")
    private String consigneeAddress;
    @ApiModelProperty("收货地址经纬度(经度，纬度)")
    private String mapCoordinate;
    @ApiModelProperty("门店经纬度(经度，纬度)")
    private String shopMapCoordinate;
    @ApiModelProperty("app订单状态（1：待配送；2：待自取；3：已完成；4：已取消；5：已失效；6：待退款；7：已退款）")
    private Integer appOrderStatus;
    @ApiModelProperty("订单状态描述（1：待配送；2：待自取；3：已完成；4：已取消；5：已失效；6：待退款；7：已退款）")
    private String orderStatusStr;
    @ApiModelProperty("取餐方式（1：外卖配送，2：到店自取）")
    private Integer deliveryWay;
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
    @ApiModelProperty("配送/取餐时间")
    private LocalDateTime deliveryTime;
    @ApiModelProperty("配送/取餐日期")
    private String deliveryDate;
    @ApiModelProperty("订单详情")
    private OrdersDetailMerchantDTO orderInfo;
}
