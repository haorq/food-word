package com.meiyuan.catering.merchant.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 店铺所在市查询条件DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("店铺所在市查询条件DTO")
public class ShopCityQueryDTO {
    @ApiModelProperty("关键字：市名称")
    private String keyword;
}
