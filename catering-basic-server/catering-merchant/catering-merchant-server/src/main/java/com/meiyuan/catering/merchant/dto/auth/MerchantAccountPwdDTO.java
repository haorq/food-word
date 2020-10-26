package com.meiyuan.catering.merchant.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author MeiTao
 * @Date 2020/7/1 0001 11:50
 * @Description 简单描述 :  商户登陆账号密码修改DTO
 * @Since version-1.0.0
 */
@Data
@ApiModel("商户登陆账号密码修改DTO")
public class MerchantAccountPwdDTO{

        @ApiModelProperty(value = "手机号")
        private String phone;

        @ApiModelProperty(value = "新密码")
        private String password;

        @ApiModelProperty(value = "短信验证码")
        private String smsCode;

        @ApiModelProperty(value = "原密码")
        private String oldPassWord;

        @ApiModelProperty(value = "账号类型： 1:商户，2：门店")
        private Integer accountType;

        @ApiModelProperty(value = "账号类型id",hidden = true)
        private Long accountTypeId;
}







