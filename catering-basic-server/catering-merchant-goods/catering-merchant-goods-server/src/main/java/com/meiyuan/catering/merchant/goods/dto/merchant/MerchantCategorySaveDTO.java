package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description：商品分类新增或修改上传参数
 * @author yy
 * @date 2020/7/7
 */
@Data
@ApiModel("商户分类-新增/修改")
public class MerchantCategorySaveDTO implements Serializable {

    private static final long serialVersionUID = 1101L;

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;

    @ApiModelProperty("类目名称")
    private String categoryName;

    @ApiModelProperty("类目图片")
    private String categoryPicture;

    @ApiModelProperty("类目描述")
    private String categoryDescribe;

    @ApiModelProperty("1-新增 2-默认")
    private Integer defaulCategory;

}
