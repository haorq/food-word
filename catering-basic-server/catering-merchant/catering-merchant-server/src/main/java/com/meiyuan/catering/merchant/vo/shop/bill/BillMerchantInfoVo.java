package com.meiyuan.catering.merchant.vo.shop.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("对账报表门店品牌搜索下拉列表")
public class BillMerchantInfoVo {

    @ApiModelProperty("门店Id【要传的】")
    private String shopId;

    @ApiModelProperty("门店名")
    private String shopName;

    @ApiModelProperty("品牌Id【要传的】")
    private String merchantId;

    @ApiModelProperty("品牌名")
    private String merchantName;

}
