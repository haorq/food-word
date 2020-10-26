package com.meiyuan.catering.goods.dto.goods;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/9/7 9:57
 */
@Data
public class GoodsSortDTO {

    @ApiModelProperty("商品排序最大值")
    private Integer sort;
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("门店id")
    private Long goodsId;
}
