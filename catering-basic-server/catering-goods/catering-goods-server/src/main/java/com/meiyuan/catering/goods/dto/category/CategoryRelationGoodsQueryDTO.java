package com.meiyuan.catering.goods.dto.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wxf
 * @date 2020/6/1 16:15
 * @description 简单描述
 **/
@Data
@ApiModel("分类详情对应的商品查询参数数据模型")
public class CategoryRelationGoodsQueryDTO {
    @ApiModelProperty("分类id")
    private Long id;
    @ApiModelProperty("排序字段 sort-排序号")
    private String sortField;
    @ApiModelProperty("排序顺序 desc-降序 asc-升序")
    private String sortOrder;
}
