package com.meiyuan.catering.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author lhm
 * @date 2020/3/26 17:43
 **/
@Data
@ApiModel("新用户id--mq")
public class UserIdDTO implements Serializable {

    @ApiModelProperty(value = "推荐人ID（老用户）")
    private Long referrerId;

    @ApiModelProperty(value = "推荐人用户类型")
    private Integer referrerType;

    @ApiModelProperty(value = "（新用户）")
    private Long userId;

    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "地推员ID")
    private Long groundPusherId;

    @ApiModelProperty(value = "扫码进入地推员的用户id")
    private Long groundUserId;

    @ApiModelProperty(value = "手机号")
    private String phone;
}
