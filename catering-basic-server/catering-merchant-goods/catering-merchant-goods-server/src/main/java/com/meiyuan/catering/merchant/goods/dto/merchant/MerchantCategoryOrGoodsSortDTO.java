package com.meiyuan.catering.merchant.goods.dto.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meiyuan.catering.core.util.BigDecimalSort;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : lhm
 * @description 描述
 * @date : 2020/8/21 15:17
 */

@Data
@ApiModel("商户分类-APP--排序")
public class MerchantCategoryOrGoodsSortDTO {

    @ApiModelProperty("商品修改排序时，需传入修改商品的分类id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long categoryId;

    @ApiModelProperty("id--1，分类修改为分类id，商品修改为商品id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long firstId;

    @ApiModelProperty(value = "分类排序号--1")
    private Integer firstSort;


    @ApiModelProperty(value = "分类排序号--2")
    private Integer secondSort;

    @ApiModelProperty("1.上移 2.下移")
    private Integer upOrDown;


    @ApiModelProperty(value = "门店id",hidden = true)
    private Long shopId;

    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;


}
