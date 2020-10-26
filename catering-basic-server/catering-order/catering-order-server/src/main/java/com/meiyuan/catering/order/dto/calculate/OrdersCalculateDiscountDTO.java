package com.meiyuan.catering.order.dto.calculate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单计算优惠信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("订单计算优惠信息——微信")
public class OrdersCalculateDiscountDTO {
    @ApiModelProperty("优惠类型（1：满减卷；2：代金卷）")
    private Integer discountType;
    @ApiModelProperty("优惠源ID（根据类型不同区分）")
    private Long discountId;
    @ApiModelProperty("优惠源编号")
    private String discountNumber;
    @ApiModelProperty("优惠源名称")
    private String discountName;
    @ApiModelProperty("品牌名称")
    private String merchantName;
    @ApiModelProperty("优惠折扣比例（折扣详细数值，如85折为0.85）")
    private BigDecimal discountRate;
    @ApiModelProperty("优惠使用积分")
    private Integer discountScore;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;
    @ApiModelProperty("实际优惠金额（优惠金额大于订单金额时优惠金额取订单金额）")
    private BigDecimal discountAmountActual;
    @ApiModelProperty(value = "使用条件：1：订单优惠；2：商品优惠")
    private Integer usefulCondition;
    @ApiModelProperty(value = "消费限制条件:满多少元可使用-1：不限制")
    private BigDecimal consumeCondition;
    @ApiModelProperty(value = "是否限制具体的商品使用:1-不限制；2-限制具体商品；3-限制商品类型")
    private Integer goodsLimit;
    @ApiModelProperty(value = "使用有效期开始时间")
    private LocalDateTime useBeginTime;
    @ApiModelProperty(value = "使用有效期结束时间")
    private LocalDateTime useEndTime;
    @ApiModelProperty(value = "商品分类集合")
    private List<DiscountGoodsCategoryDTO> goodsCategoryItem;
    @ApiModelProperty(value = "商品集合")
    private List<DiscountGoodsInfoDTO> goodsItem;
    @ApiModelProperty(value = "订单商品优惠均摊信息")
    List<OrdersCalculateGoodsDiscountDTO> goodsDiscountList;
}
