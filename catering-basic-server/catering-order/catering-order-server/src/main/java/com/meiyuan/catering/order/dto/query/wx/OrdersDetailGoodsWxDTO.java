package com.meiyuan.catering.order.dto.query.wx;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单详情商品信息——后台
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ApiModel("订单详情商品信息——后台")
public class OrdersDetailGoodsWxDTO {
    @ApiModelProperty("订单商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderGoodsId;
    @ApiModelProperty("商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;
    @ApiModelProperty("商品图片")
    private String goodsPicture;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品类型（1--普通商品，2--秒杀商品，3--团购商品）")
    private Integer goodsType;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("商品原价")
    private BigDecimal storePrice;
    @ApiModelProperty("商品销售价")
    private BigDecimal salesPrice;
    @ApiModelProperty("商品总原价")
    private BigDecimal goodsStorePrice;
    @ApiModelProperty("优惠前商品金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("优惠后商品金额")
    private BigDecimal discountLaterFee;
    @ApiModelProperty("商品规格描述")
    private String goodsSpecificationDesc;
    @ApiModelProperty("商品规格[1:统一规格，2：多规格]")
    private Integer goodsSpecType;
    @ApiModelProperty("是否赠品（0：不是赠品；1：是赠品）")
    private Boolean gifts;
    /** 赠品活动id */
    private Long giftsActivityId;
    /** 拼单用户名称 */
    private String shareBillUserName;
    /** 拼单用户头像 */
    private String shareBillUserAvatar;
    @ApiModelProperty(value = "拼单人ID",hidden = true)
    private Long    shareBillUserId;
}
