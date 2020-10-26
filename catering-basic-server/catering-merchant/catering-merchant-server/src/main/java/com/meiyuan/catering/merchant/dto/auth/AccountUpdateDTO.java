package com.meiyuan.catering.merchant.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:50
 * @Description 简单描述 :  登陆账号密码修改DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("登陆账号密码修改DTO")
public class AccountUpdateDTO {

        @ApiModelProperty(value = "手机号")
        private String phone;

        @ApiModelProperty(value = "账号类型对应id")
        private Long accountTypeId;

        @ApiModelProperty(value = "密码[已加密码]")
        private String password;
}







