package com.meiyuan.catering.goods.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/6/1 15:38
 * @description 简单描述
 **/
@Data
@ApiModel("修改分类参数模型")
public class UpdateCategorySortDTO {
    @ApiModelProperty("分类id")
    private Long categoryId;
    @ApiModelProperty("变更的排序号")
    private Integer sort;
}
