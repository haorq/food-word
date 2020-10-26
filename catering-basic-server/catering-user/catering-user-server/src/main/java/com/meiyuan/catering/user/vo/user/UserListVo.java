package com.meiyuan.catering.user.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/19 14:48
 **/
@Data
@ApiModel("个人查询listVo")
public class UserListVo implements Serializable {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("地区")
    private String area;

    @ApiModelProperty("详细地址")
    private String addressDetail;

    @ApiModelProperty("联系人")
    private String name;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("积分")
    private Integer integral;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("地推员code")
    private String pusherCode;

    @ApiModelProperty("用户来源：0:自然流量 1:地推 2:被邀请")
    private Integer pullNewUser;
}
