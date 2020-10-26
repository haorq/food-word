package com.meiyuan.catering.merchant.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/5/19 0019 11:44
 * @Description 简单描述 : 店铺所有信息DTO
 * @Since version-1.1.0
 */
@Data
@ApiModel("店铺所有信息DTO")
public class ShopQueryDTO {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("商户名称")
    private String merchantName;
    @ApiModelProperty(value = "省编码",hidden = true)
    private String addressProvinceCode;
    @ApiModelProperty(value = "市编码")
    private String addressCityCode;
    @ApiModelProperty(value = "区编码",hidden = true)
    private String addressAreaCode;
    @ApiModelProperty(value = "店铺状态:1：启用，2：禁用",hidden = true)
    private Integer shopStatus;
    @ApiModelProperty(value = "店铺服务:1;外卖小程序,2:食堂美食城",hidden = true)
    private Integer shopService;

}
