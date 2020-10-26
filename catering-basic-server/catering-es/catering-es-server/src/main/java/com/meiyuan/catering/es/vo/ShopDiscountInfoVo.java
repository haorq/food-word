package com.meiyuan.catering.es.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date 2020/8/6 0006 16:56
 * @Description 简单描述 : 店铺优惠信息Vo
 * @Since version-1.3.0
 */
@Data
@ApiModel("店铺优惠信息Vo")
public class ShopDiscountInfoVo {

    @ApiModelProperty("优惠类型:1：折扣商品，2：进店领券，3：满减优惠券(店外发券/平台自动发券)，4，秒杀活动，5：团购活动,6:配送费优惠")
    private Integer discountType;

    @ApiModelProperty("优惠具体信息")
    private List<String> discountStr;

    @ApiModelProperty(value = "门店id", hidden = true)
    private String shopId;


}
