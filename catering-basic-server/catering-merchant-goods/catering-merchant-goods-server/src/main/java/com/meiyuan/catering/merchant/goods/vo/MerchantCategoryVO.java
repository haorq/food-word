package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：商品分类列表返回参数
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("商户商品-分类列表")
public class MerchantCategoryVO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    @ApiModelProperty(value = "1-新增 2-默认")
    private Integer defaultCategory;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "关联商品数")
    private Integer goodsCount;

    @ApiModelProperty(value = "商品分类排序号")
    private Integer sort;
}
