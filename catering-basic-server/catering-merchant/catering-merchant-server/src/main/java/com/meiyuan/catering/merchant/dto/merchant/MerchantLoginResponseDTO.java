package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoozu
 * @description 商户登录
 * @date 2020/3/2011:08
 * @since v1.0.0
 */
@Data
@ApiModel("商户登录结果")
public class MerchantLoginResponseDTO {
    @ApiModelProperty(value = "登录成功Token：header中传的name为X-Catering-Token")
    private String token;

    @ApiModelProperty("店铺信息")
    private ShopResponseDTO shop;
}
