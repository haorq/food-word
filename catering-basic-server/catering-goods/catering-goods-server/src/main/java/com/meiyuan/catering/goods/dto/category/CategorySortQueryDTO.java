package com.meiyuan.catering.goods.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/6/1 11:34
 * @description 简单描述
 **/
@Data
@ApiModel
public class CategorySortQueryDTO {
    @ApiModelProperty("验证类型 1-分类 2-分类里面对应的商品")
    private Integer type;
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("商品id, 验证类型是分类 可以不传 商品必传")
    private Long goodsId;
    @ApiModelProperty("排序")
    private Integer sort;
}
