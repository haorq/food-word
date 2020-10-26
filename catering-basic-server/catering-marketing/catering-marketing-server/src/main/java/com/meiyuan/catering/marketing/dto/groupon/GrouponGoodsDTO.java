package com.meiyuan.catering.marketing.dto.groupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author luohuan
 * @date 2020/3/16
 **/
@Data
@ApiModel("团购商品DTO")
public class GrouponGoodsDTO {

    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty("商品ID")
    private Long goodsId;

    @NotNull(message = "活动价格不能为空")
    @ApiModelProperty("活动价格")
    private BigDecimal activityPrice;

    @ApiModelProperty("销售价")
    private BigDecimal salesPrice;

    @NotNull(message = "起团数量不能为空")
    @ApiModelProperty("起团数量")
    private Integer minGrouponQuantity;

    @NotNull(message = "最低购买数量不能为空")
    @ApiModelProperty("最低购买数量")
    private Integer minQuantity;

    @ApiModelProperty("商品规格编码")
    private String skuCode;

    @ApiModelProperty("商品规格值")
    private String propertyValue;
}
