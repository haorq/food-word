package com.meiyuan.catering.user.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @date 2020/3/23 11:39
 **/
@Data
@ApiModel("用户信息")
public class UserInfoDTO implements Serializable {


    @ApiModelProperty("微信昵称")
    private String nickName;
    @ApiModelProperty("微信头像")
    private String avatarUrl;
    @ApiModelProperty("国家")
    private String country;
    @ApiModelProperty("省")
    private String province;
    @ApiModelProperty("市")
    private String city;
    @ApiModelProperty("1：男；2-女；3-其他")
    private Integer gender;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("推荐人id（推荐有奖时才需要）")
    private Long referrerId;
    @ApiModelProperty("当前推荐人用户类型")
    private Integer userType;
    @ApiModelProperty("地推员id")
    private Long groundPusherId;

}
