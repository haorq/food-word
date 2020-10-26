package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * description：
 *
 * @author yy
 * @version 1.3.0
 * @date 2020/8/5 9:32
 */
@Data
@ApiModel("用户信息详细信息")
public class UserDetailVO {

    @ApiModelProperty("用户缓存信息token")
    private String token;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("联系人")
    private String userName;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("用户类型：1：企业用户，2：个人用户")
    private Integer userType;

    @ApiModelProperty("微信头像")
    private String avatarUrl;

    @ApiModelProperty("微信昵称")
    private String nickName;

    @ApiModelProperty("1：男；2-女；3-其他")
    private Integer gender;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("是否设置支付密码")
    private Boolean isPayPassWord;

    @ApiModelProperty("余额")
    private BigDecimal balance;

    @ApiModelProperty("我的优惠券数量")
    private Integer ticketNumber;

    @ApiModelProperty("用户类型中文意思")
    private String userTypeStr;

    @ApiModelProperty("性别中文意思")
    private String sex;
}
