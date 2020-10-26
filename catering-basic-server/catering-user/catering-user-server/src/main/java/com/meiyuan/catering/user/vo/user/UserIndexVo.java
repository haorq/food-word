package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lhm
 * @date 2020/3/27 15:52
 **/
@Data
@ApiModel("c端--我的用户信息")
public class UserIndexVo implements Serializable {
    @ApiModelProperty("我的余额")
    private BigDecimal balance;
    @ApiModelProperty("我的积分")
    private Integer integral;
    @ApiModelProperty("我的优惠券--只展示未使用")
    private Integer ticket;
    @ApiModelProperty("用户等级")
    private Integer userLevel;
    @ApiModelProperty("1：企业用户，2：个人用户")
    private Integer userType;
    @ApiModelProperty("微信头像")
    private String avatar;
    @ApiModelProperty("微信昵称")
    private String nickname;
    @ApiModelProperty("企业账号")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long companyId;





}
