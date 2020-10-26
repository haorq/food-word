package com.meiyuan.catering.marketing.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author GongJunZheng
 * @date 2020/08/25 15:08
 * @description 营销商品分类信息修改DTO
 **/

@Data
@ApiModel(value = "营销商品分类信息修改DTO")
public class MarketingGoodsCategoryUpdateDTO {

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "商品分类ID")
    private Long categoryId;
    @ApiModelProperty(value = "商品分类名称")
    private String categoryName;

}
