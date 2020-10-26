package com.meiyuan.catering.merchant.goods.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/25 11:33
 */
@Data
public class GoodsSortMaxDTO {

    @ApiModelProperty("商品排序最大值")
    private Integer sort;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("门店id")
    private Long shopId;
}
