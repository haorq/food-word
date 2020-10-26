package com.meiyuan.catering.user.vo.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @since 2020-03-18
 */
@Data
@ApiModel("积分记录列表Vo")
public class IntegralRecordListVo implements Serializable {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("用户昵称")
    private String userNickName;
    @ApiModelProperty("积分数量")
    private Integer integral;
    @ApiModelProperty("获取途径")
    private String reason;
    @ApiModelProperty("用户手机号")
    private String userPhone;
    @ApiModelProperty("用户类型 1：企业用户，2：个人用户")
    private Integer userType;
    @ApiModelProperty("记录类型 1:增加 2:减少")
    private Integer type;

}
