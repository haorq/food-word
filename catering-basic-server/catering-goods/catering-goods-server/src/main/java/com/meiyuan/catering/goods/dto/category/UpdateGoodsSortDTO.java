package com.meiyuan.catering.goods.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/6/1 18:28
 * @description 简单描述
 **/
@Data
@ApiModel("修改商品排序参数模型")
public class UpdateGoodsSortDTO {
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("变更的排序号")
    private Integer sort;
}
