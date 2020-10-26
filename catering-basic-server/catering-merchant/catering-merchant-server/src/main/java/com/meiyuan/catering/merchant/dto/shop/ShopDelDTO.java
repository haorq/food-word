package com.meiyuan.catering.merchant.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/5/19 0019 11:44
 * @Description 简单描述 : 店铺删除接收参数DTO
 * @Since version-1.1.0
 */
@Data
@ApiModel("店铺删除接收参数DTO")
public class ShopDelDTO {
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty(value = "商户id",hidden = true)
    private Long merchantId;
}
