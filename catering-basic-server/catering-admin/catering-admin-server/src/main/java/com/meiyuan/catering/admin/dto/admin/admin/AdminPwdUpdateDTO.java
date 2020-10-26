package com.meiyuan.catering.admin.dto.admin.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/20 15:56
 **/
@Data
@ApiModel("管理员修改密码dto")
public class AdminPwdUpdateDTO implements Serializable {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("原密码")
    private String oldPassword;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty("验证码")
    private String vefiyCode;


}
