package com.meiyuan.catering.merchant.goods.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/09/03 11:09
 * @description 营销特价商品活动创建/编辑商品选择DTO
 **/

@Data
@ApiModel(value = "营销特价商品活动创建/编辑商品选择DTO")
public class MarketingSpecialGoodsSelectDTO {

    @ApiModelProperty("商品分类ID")
    private Long categoryId;
    @ApiModelProperty("商品名称")
    private String goodsName;
}
