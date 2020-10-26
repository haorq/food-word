package com.meiyuan.catering.marketing.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/06 15:08
 * @description 简单描述
 **/

@Data
@ApiModel(value = "秒杀活动商品详情VO")
public class MarketingSeckillGoodsDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品sku")
    private String skuCode;
    @ApiModelProperty(value = "商品规格值")
    private String propertyValue;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "原价")
    private BigDecimal storePrice;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "每场秒杀总数量")
    private Integer quantity;
    @ApiModelProperty(value = "最低购买数")
    private Integer minQuantity;
    @ApiModelProperty(value = "最高购买数")
    private Integer limitQuantity;
    @ApiModelProperty(value = "商品分类ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

    public String getLimitQuantity() {
        if(limitQuantity == -1) {
            return "";
        }
        return limitQuantity.toString();
    }
}
