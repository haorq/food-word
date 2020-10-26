package com.meiyuan.catering.merchant.vo.shop.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("下拉城市列表")
public class ShopBillCityVo {

    @ApiModelProperty("门店Id")
    private String cityName;

    @ApiModelProperty("城市code(选中传的)")
    private String cityCode;

    @ApiModelProperty("该城市的门店数量")
    private Integer shopCount;
}
