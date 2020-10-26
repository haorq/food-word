package com.meiyuan.catering.admin.dto.admin.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author lhm
 * @date 2020/3/2 13:58
 **/
@Data
@ApiModel("登录vo")
public class UserLoginDto {

    @ApiModelProperty(value = "账号或手机号", required = true)
    @NotBlank(message = "账号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;

}
