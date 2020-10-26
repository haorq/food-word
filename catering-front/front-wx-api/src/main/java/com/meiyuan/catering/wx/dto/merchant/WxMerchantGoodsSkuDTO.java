package com.meiyuan.catering.wx.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wxf
 * @date 2020/3/16 11:33
 * @description 商品SKU模型
 **/
@Data
@ApiModel("商品SKU模型")
public class WxMerchantGoodsSkuDTO {
    @ApiModelProperty("id 新增不传")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("sku编码 新增不传")
    private String skuCode;
    @ApiModelProperty("规格值")
    private String propertyValue;
    @ApiModelProperty("原价")
    private BigDecimal marketPrice;
    @ApiModelProperty("现价")
    private BigDecimal salesPrice;
    @ApiModelProperty("企业价")
    private BigDecimal enterprisePrice;
    @ApiModelProperty("加入购物车数量")
    private Integer selectedNum;
    @ApiModelProperty("最低购买")
    private Integer lowestBuy;
    @ApiModelProperty("最多购买 -1不限购")
    private Integer highestBuy;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "剩余库存")
    private Integer residualInventory;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "每单限x份优惠")
    private Integer discountLimit;
    /**
     * @since v1.2.0
     */
    @ApiModelProperty(value = "售卖渠道 1.外卖小程序 2:食堂 3.全部")
    private Integer salesChannels;
    /**
     * @since v1.3.0
     */
    @ApiModelProperty("折扣")
    private String discountLabel;

    @ApiModelProperty(value = "V1.4.0 特价商品活动中的特价商品个人折扣值")
    private BigDecimal specialNumber;
    @ApiModelProperty(value = "V1.4.0 特价商品活动中的特价商品企业折扣值")
    private BigDecimal enterpriseSpecialNumber;
    @ApiModelProperty(value = "V1.4.0 特价商品活动中的特价商品起售数量")
    private Integer minQuantity;
    @ApiModelProperty(value = "V1.4.0 特价活动id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long specialId;
    @ApiModelProperty(value = "V1.4.0 是否是特价商品 false-否 true-是")
    private Boolean specialState;

}
