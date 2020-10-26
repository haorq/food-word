package com.meiyuan.catering.merchant.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/6/11 0011 13:56
 * @Description 简单描述 : 店铺所在市集合DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("店铺所在市集合DTO")
public class ShopCityListDTO {
    @ApiModelProperty("市首字母")
    private String firstLetter;
    @ApiModelProperty("市名称")
    private List<ShopCityDTO> addressCity;
}
