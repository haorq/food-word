package com.meiyuan.catering.order.dto.calculate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 订单计算商品信息——微信
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 **/
@Data
@ToString(callSuper = true)
@ApiModel("订单计算商品信息——内部调用")
public class OrdersCalculateGoodsDTO {
    @ApiModelProperty(value = "(团购、秒杀、普通)商品Id")
    private Long goodsId;
    @ApiModelProperty("商品图片")
    private String goodsPicture;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品类型（1--普通商品，2--秒杀商品；3--团购商品；）")
    private Integer goodsType;
    @ApiModelProperty("商品SPU编码")
    private String goodsSpuCode;
    @ApiModelProperty("商品SKU编码")
    private String goodsSkuCode;
    @ApiModelProperty("商品购买数量")
    private Integer quantity;
    @ApiModelProperty("市场单价")
    private BigDecimal storePrice;
    @ApiModelProperty("销售单价")
    private BigDecimal salesPrice;
    @ApiModelProperty("优惠前商品金额")
    private BigDecimal discountBeforeFee;
    @ApiModelProperty("优惠后商品金额")
    private BigDecimal discountLaterFee;
    /**
     * @version 1.2.0
     * @author lh
     * @desc 单个商品总金额。每单限多少分优惠有新增字段
     */
    @ApiModelProperty("单个商品总金额")
    private BigDecimal goodsTotalPrice;
    @ApiModelProperty("优惠金额")
    private BigDecimal discountFee;
    /**
     * @version 1.5.0
     * @author lh
     * @desc 餐盒费
     */
    private BigDecimal packPrice;
    @ApiModelProperty("商品分类ID")
    private Long categoryId;
    @ApiModelProperty("商品分类名称")
    private String categoryName;
    @ApiModelProperty("商品规格描述")
    private String goodsSpecificationDesc;
    @ApiModelProperty("商品标签")
    private String goodsLabelName;
    @ApiModelProperty("拼单用户id")
    private Long shareBillUserId;
    @ApiModelProperty("拼单用户名称")
    private String shareBillUserName;
    @ApiModelProperty("拼单用户头像")
    private String shareBillUserAvatar;
    @ApiModelProperty("是否赠品（0：不是赠品[默认]；1：是赠品）")
    private Boolean gifts;
    @ApiModelProperty("每单限X份优惠")
    private Integer discountLimit;

    /**
     * 秒杀场次Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long seckillEventId;

    @ApiModelProperty(value = "商品规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;

}
