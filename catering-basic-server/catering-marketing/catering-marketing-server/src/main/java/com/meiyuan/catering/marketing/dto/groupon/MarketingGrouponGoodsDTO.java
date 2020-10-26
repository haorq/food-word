package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author GongJunZheng
 * @date 2020/08/07 11:08
 * @description 营销团购活动新增/编辑的商品列表DTO
 **/

@Data
@ApiModel(value = "营销团购活动新增/编辑的商品列表DTO")
public class MarketingGrouponGoodsDTO {

    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty("商品ID")
    private Long goodsId;

    @NotNull(message = "活动价格不能为空")
    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("销售价/原价")
    private BigDecimal storePrice;

    @NotNull(message = "起团数量不能为空")
    @ApiModelProperty("起团数量")
    @Range(min = 1, max = 999999, message = "起团数量值错误")
    private Integer minGrouponQuantity;

    @ApiModelProperty("最低购买数量")
    private Integer minQuantity;

    @NotBlank(message = "商品SKU不能为空")
    @ApiModelProperty("商品规格编码")
    private String skuCode;

    @ApiModelProperty("商品规格值")
    private String propertyValue;

    @ApiModelProperty(value = "商品排序序号", hidden = true)
    private Integer goodsSort;

    public Integer getMinQuantity() {
        if(null == minQuantity) {
            minQuantity = 1;
        }
        return minQuantity;
    }
}
