package com.meiyuan.catering.merchant.goods.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/3 0003 14:42
 * @Description 简单描述 :  店铺折扣商品查询参数dto
 * @Since version-1.2.0
 */
@Data
@ApiModel(value = "店铺折扣商品查询参数dto")
public class ShopDiscountGoodsDTO {
    @ApiModelProperty(value = "店铺ids")
    private List<Long> shopIds;

    @ApiModelProperty(value = "用户类型:1:企业，2：个人")
    private Integer userType;
}
