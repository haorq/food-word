package com.meiyuan.catering.admin.dto.admin.profile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date  2020/3/2 0002 16:52
 */
@Data
@ApiModel("管理员密码修改vo")
public class AdminPassworldUpdateDto {

    @ApiModelProperty(value = "旧密码")
    private String oldPassword;
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
