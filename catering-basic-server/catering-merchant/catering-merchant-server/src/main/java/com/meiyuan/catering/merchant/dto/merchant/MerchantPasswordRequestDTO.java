package com.meiyuan.catering.merchant.dto.merchant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yaoozu
 * @description 修改密码
 * @date 2020/3/2011:08
 * @since v1.0.0 v1.1.0
 */
@Data
@ApiModel("修改密码DTO")
public class MerchantPasswordRequestDTO {
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "新密码",required = true)
    @NotNull
    private String password;

    @ApiModelProperty(value = "短信验证码")
    private String smsCode;

    @ApiModelProperty(value = "原密码")
    private String oldPassWord;

}
