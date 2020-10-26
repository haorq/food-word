package com.meiyuan.catering.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/23 11:38
 **/
@Data
@ApiModel("微信用户登录信息")
public class WxLoginInfoDTO implements Serializable {
    @ApiModelProperty(value = "code",required = true)
    @NotBlank(message = "code不能为空")
    private String code;
    private UserInfoDTO userInfoDTO;
}
