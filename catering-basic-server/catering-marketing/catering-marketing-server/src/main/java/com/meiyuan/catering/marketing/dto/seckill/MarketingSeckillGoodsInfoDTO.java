package com.meiyuan.catering.marketing.dto.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName MarketingSeckillGoodsInfoDTO
 * @Description
 * @Author gz
 * @Date 2020/3/16 17:37
 * @Version 1.1
 */
@Data
public class MarketingSeckillGoodsInfoDTO{
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品市场价--原价")
    private BigDecimal storePrice;

    @ApiModelProperty(value = "商品图片")
    private String goodsPicture;
    /**
     * 商品sku
     */
    @ApiModelProperty(value = "商品sku")
    private String sku;
    /**
     * 商品数量/发行数量
     */
    @ApiModelProperty(value = "商品数量/发行数量--库存")
    private Integer quantity;
    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitQuantity;
    @ApiModelProperty(value = "起购数量")
    private Integer minQuantity;

    /**
     * 活动价
     */
    @ApiModelProperty(value = "活动价")
    private BigDecimal salesPrice;
    /**
     * 商品总库存
     */
    @ApiModelProperty(value = "商品总库存")
    private Integer totalQuantity;
    @ApiModelProperty(value = "规格值")
    private String skuValue;
    @ApiModelProperty(value = "商品分类名称")
    private String goodsCategoryName;

    public String getLimitQuantity() {
        if(limitQuantity == null || limitQuantity == -1) {
            return "";
        }
        return limitQuantity.toString();
    }

    @ApiModelProperty(value = "删除标记")
    private Boolean del;
}
