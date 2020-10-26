package com.meiyuan.catering.merchant.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * @Author MeiTao
 * @Description 商户赠品 查询条件TO
 * @Date  2020/3/16 0016 14:53
 */
@Data
@ApiModel("登录vo")
public class MerchantPcUserDTO {

    @ApiModelProperty(value = "账号或手机号", required = true)
    @NotBlank(message = "账号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @ApiModelProperty(value = "账号类型： 1:商户，2：门店", required = true)
    private Integer accountType;

    @ApiModelProperty(value = "三天内自动登录")
    private Boolean autoLogin;
}
