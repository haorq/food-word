package com.meiyuan.catering.user.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/23 15:40
 **/
@Data
@ApiModel("个人信息详情")
public class UserDetailInfoVo implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("联系人")
    private String userName;
    @ApiModelProperty("openId")
    private String openId;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("1：企业用户，2：个人用户")
    private Integer userType;
    @ApiModelProperty("微信头像")
    private String avatar;
    @ApiModelProperty("微信昵称")
    private String nickName;
    @ApiModelProperty("1：男；2-女；3-其他")
    private Integer gender;
    @ApiModelProperty("企业名称")
    private String companyName;
    @ApiModelProperty("是否设置支付密码")
    private Boolean isPayPassWord;
}
