package com.meiyuan.catering.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/4 16:08
 */
@Data
@ApiModel("微信用户获取手机号参数")
public class UserSessionInfoDTO implements Serializable {

    private static final long serialVersionUID = 202008111616110501L;

    @ApiModelProperty(value = "微信用户code", required = true)
    private String code;

    @ApiModelProperty(value = "加密信息", required = true)
    private String encryptedData;

    @ApiModelProperty(value = "iv", required = true)
    private String iv;
}
