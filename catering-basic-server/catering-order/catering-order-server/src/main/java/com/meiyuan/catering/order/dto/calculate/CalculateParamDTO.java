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
@ApiModel("计算请求参数——微信")
public class CalculateParamDTO extends WxUserTokenDTO {
    private static final long serialVersionUID = 3384982463060377530L;

    @ApiModelProperty(value = "商户ID", required = true)
    private Long merchantId;
    @ApiModelProperty(value = "门店ID", required = true)
    private Long shopId;

    @ApiModelProperty(value = "取餐方式（1：外卖配送，2：到店自取）", required = true)
    private Integer deliveryWay;

    @ApiModelProperty("经纬度，选择【配送方式】时必传")
    private String mapCoordinate;

    @ApiModelProperty(value = "收货信息Id")
    private Long deliveryId;

    @ApiModelProperty(value = "自提点ID，选择【自取方式】时必传")
    private Long deliveryShopId;

    @ApiModelProperty(value = "自提点名称，选择【自取方式】时必传")
    private String deliveryShopName;

    @ApiModelProperty(value = "用户优惠卷Id")
    private Long couponsId;
    @ApiModelProperty(value = "用户优惠卷Id（门店）")
    protected Long couponsWithShopId;

    @ApiModelProperty(value = "预计送达、取餐时间 ")
    private LocalDateTime estimateTime;

    @ApiModelProperty(value = "预计送达、取餐截止时间")
    private LocalDateTime estimateEndTime;

    @ApiModelProperty("立即送达时间")
    private String immediateDeliveryTime;

    @ApiModelProperty(value = "结算类型：1：菜单购物车，2：普通、秒杀购物车，3：拼单购物车，4：团购", required = true)
    private Integer calculateType;

    @ApiModelProperty("餐盒费【团购的时候必传】")
    private BigDecimal packPrice;

}
