package com.meiyuan.catering.marketing.vo.groupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/07 13:08
 * @description 团购活动详情-团购商品VO
 **/

@Data
@ApiModel(value = "团购活动详情-团购商品VO")
public class MarketingGrouponGoodsDetailVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("商品ID")
    private Long goodsId;
    @ApiModelProperty("商品名称")
    private String goodsName;
    @ApiModelProperty("商品规格sku编码")
    private String skuCode;
    @ApiModelProperty("商品规格sku值")
    private String propertyValue;
    @ApiModelProperty("规格类型 1-统一规格 2-多规格")
    private Integer goodsSpecType;
    @ApiModelProperty("市场价（即原价）")
    private BigDecimal storePrice;
    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;
    @ApiModelProperty("起团数量")
    private Integer minGrouponQuantity;
    @ApiModelProperty("最低购买数量")
    private Integer minQuantity;
    @ApiModelProperty(value = "商品分类ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;
    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

}
