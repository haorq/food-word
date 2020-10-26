package com.meiyuan.catering.merchant.dto.shop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/5/19 0019 11:44
 * @Description 简单描述 : 平台报表商户查询结果DTO
 * @Since version-1.1.0
 */
@Data
@ApiModel("平台报表商户查询结果DTO")
public class MerchantShopInfoDTO {
    @ApiModelProperty("门店id")
    private Long shopId;
    @ApiModelProperty("商户id")
    private Long merchantId;
    @ApiModelProperty("门店名称")
    private String shopName;
    @ApiModelProperty("商户名称")
    private String merchantName;
}
