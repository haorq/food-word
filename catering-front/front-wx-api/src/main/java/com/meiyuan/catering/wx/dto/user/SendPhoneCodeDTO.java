package com.meiyuan.catering.wx.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/30 12:03
 **/
@Data
@ApiModel
public class SendPhoneCodeDTO implements Serializable {

    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "iv")
    private String code;
}
