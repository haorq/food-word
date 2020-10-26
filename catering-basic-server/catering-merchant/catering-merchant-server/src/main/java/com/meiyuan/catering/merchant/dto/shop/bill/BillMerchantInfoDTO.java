package com.meiyuan.catering.merchant.dto.shop.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("对账报表详情门店品牌搜索接收参数DTO")
public class BillMerchantInfoDTO {

    @ApiModelProperty("要搜索的字符串")
    private String name;

    @ApiModelProperty("搜索类型(1:品牌名字或者编码，2：门店)")
    private int type;
}
