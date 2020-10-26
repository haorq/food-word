package com.meiyuan.catering.merchant.goods.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description：查询详情返回值
 *
 * @author yy
 * @version 1.2.0
 * @date 2020/8/3 10:48
 */
@Data
@ApiModel("商户商品-新增返回")
public class MerchantCategorySaveVO {

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("类目名称")
    private String categoryName;

    @ApiModelProperty("类目图片")
    private String categoryPicture;

    @ApiModelProperty("类目描述")
    private String categoryDescribe;

    @ApiModelProperty("1-新增 2-默认")
    private Integer defaulCategory;

}
