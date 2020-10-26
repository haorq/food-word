package com.meiyuan.catering.marketing.vo.seckill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/07 10:08
 * @description 秒杀活动效果-活动商品数据VO
 **/

@Data
@ApiModel(value = "秒杀活动效果-活动商品数据VO")
public class MarketingSeckillGoodsEffectVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品sku")
    private String sku;
    @ApiModelProperty(value = "商品规格值")
    private String skuValue;
    @ApiModelProperty(value = "规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty(value = "活动价")
    private BigDecimal activityPrice;
    @ApiModelProperty(value = "秒杀商品总数量")
    private Integer quantity;
    @ApiModelProperty(value = "秒杀商品剩余数量")
    private Integer remainingQuantity;
    @ApiModelProperty(value = "售卖总数量")
    private Integer soldOutQuantity;
    @ApiModelProperty(value = "关联订单量")
    private Integer relationOrderNum;
    @ApiModelProperty(value = "商品销售额")
    private BigDecimal business;

    /**
     * 营销商品ID
     */
    @JsonIgnore
    private Long id;

}
