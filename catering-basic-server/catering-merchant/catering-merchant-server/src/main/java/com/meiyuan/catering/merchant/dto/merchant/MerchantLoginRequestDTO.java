package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yaoozu
 * @description 商户登录
 * @date 2020/3/2011:08
 * @since v1.0.0
 */
@Data
@ApiModel("商户登录DTO")
public class MerchantLoginRequestDTO {
    @ApiModelProperty(value = "手机号",required = true)
    private String phone;
    @ApiModelProperty(value = "登录密码",required = true)
    private String password;
    @ApiModelProperty(value = "设备号",required = true)
    @NotNull(message = "店铺登录设备号不能为空")
    private String deviceNumber;
}
