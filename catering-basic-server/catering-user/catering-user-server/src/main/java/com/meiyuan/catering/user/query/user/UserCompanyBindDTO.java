package com.meiyuan.catering.user.query.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/23 17:51
 **/
@Data
@ApiModel("企业用户绑定dto")
public class UserCompanyBindDTO implements Serializable {

    @ApiModelProperty("企业账号")
    @NotBlank(message = "企业账号不能为空")
    private String account;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private Long id;

}
