package com.meiyuan.catering.merchant.dto.shop.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("营业概况图表模型")
public class BillTopTenDTO {

    private String goodsId;

    @ApiModelProperty("商品名字")
    private String goodsName;

    @ApiModelProperty("商品销售的数量")
    private Integer goodsQuantity;

    private String shopId;

    @ApiModelProperty("店铺名字")
    private String shopName;

    @ApiModelProperty("店铺的销售额")
    private BigDecimal shopAmount;
}
