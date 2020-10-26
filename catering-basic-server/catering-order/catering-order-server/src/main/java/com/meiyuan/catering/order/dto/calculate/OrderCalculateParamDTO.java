package com.meiyuan.catering.order.dto.calculate;

import com.meiyuan.catering.order.dto.WxUserTokenDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单计算请求参数DTO
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Data
@ToString(callSuper = true)
@ApiModel("订单计算请求参数——微信")
public class OrderCalculateParamDTO extends WxUserTokenDTO {
    private static final long serialVersionUID = 3384982463060377530L;

    @ApiModelProperty(value = "商户ID", required = true)
    protected Long merchantId;
    @ApiModelProperty(value = "门店ID", required = true)
    protected Long shopId;

    @ApiModelProperty(value = "服务方式（1:外卖配送，2:到店自取，3:堂食正餐，4:堂食快餐，5:堂食外带）", required = true)
    protected Integer deliveryWay;
    @ApiModelProperty(value = "订单来源（1:外卖小程序，2:堂食小程序）", required = true)
    protected Integer orderSource;
    @ApiModelProperty(value = "下单类型（1:外卖，2:堂食）", required = true)
    protected Integer orderWay;

    @ApiModelProperty("经纬度，选择【配送方式】时必传")
    protected String mapCoordinate;

    @ApiModelProperty(value = "收货信息Id")
    protected Long deliveryId;

    @ApiModelProperty(value = "自提点ID，选择【自取方式】时必传")
    protected Long deliveryShopId;

    @ApiModelProperty(value = "自提点名称，选择【自取方式】时必传")
    protected String deliveryShopName;

    @ApiModelProperty(value = "取餐电话，选择【自取方式】时必传")
    protected String deliveryPhone;

    @ApiModelProperty(value = "菜单ID，【菜单购物车】必传")
    protected Long menuId;

    @ApiModelProperty(value = "拼单号，【拼单购物车】必传")
    protected String shareBillNo;

    @ApiModelProperty(value = "营销商品Id，(【团购】结算时必传)")
    protected Long goodsId;

    @ApiModelProperty(value = "商品单价，(【团购【结算时必传)")
    protected BigDecimal goodsSalesPrice;

    @ApiModelProperty(value = "商品数量，(【团购】结算时必传)")
    protected Integer goodsQuantity;

    @ApiModelProperty(value = "合计金额，(【团购】结算时必传)")
    protected BigDecimal totalAmount;

    @ApiModelProperty(value = "平台用户优惠卷Id")
    protected Long couponsId;
    @ApiModelProperty(value = "用户优惠卷Id（门店）")
    protected Long couponsWithShopId;

    @ApiModelProperty("平台优惠券实际扣减金额")
    protected BigDecimal couponsAmount;
    @ApiModelProperty("门店优惠券实际扣减金额")
    protected BigDecimal couponsAmountWithShop;


    @ApiModelProperty(value = "预计送达、取餐时间 ")
    protected LocalDateTime estimateTime;

    @ApiModelProperty(value = "预计送达、取餐截止时间")
    protected LocalDateTime estimateEndTime;

    @ApiModelProperty("立即送达时间")
    private String immediateDeliveryTime;

    @ApiModelProperty(value = "结算类型：1：菜单购物车，2：普通、秒杀购物车，3：拼单购物车，4：团购", required = true)
    protected Integer calculateType;

    @ApiModelProperty("餐盒费【团购的时候必传】")
    private BigDecimal packPrice;

}
