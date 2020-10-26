package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BigDecimalSort;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description：商品分类下拉框返回参数
 * @author yy
 * @date 2020/7/9
 */
@Data
@ApiModel("商品分类-下拉列表")
public class MerchantCategoryDownVO {

    @ApiModelProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "分类排序号")
    private Integer sort;

    @ApiModelProperty(value = "分类来源：1平台推送 2商户自创 3门店自创")
    private Integer categoryAndType;

    @ApiModelProperty("1-新增 2-默认")
    private Integer defaulCategory;

}
