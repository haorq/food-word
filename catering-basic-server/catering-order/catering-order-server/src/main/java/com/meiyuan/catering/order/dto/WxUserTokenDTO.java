package com.meiyuan.catering.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoozu
 * @description 微信用户信息
 * @date 2020/3/2111:30
 * @since v1.0.0
 */
@Data
public class WxUserTokenDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Integer gender;
    @ApiModelProperty(hidden = true)
    protected Long userId;
    @ApiModelProperty(hidden = true)
    protected String nickname;
    @ApiModelProperty(hidden = true)
    protected String phone;
    @ApiModelProperty(hidden = true)
    private String avatar;
    @ApiModelProperty(value = "用户类型,1--企业用户，2--个人用户",hidden = true)
    protected Integer userType;
}
