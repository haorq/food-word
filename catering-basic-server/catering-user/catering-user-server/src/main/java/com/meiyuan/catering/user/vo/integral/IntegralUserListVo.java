package com.meiyuan.catering.user.vo.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @date 2020/3/19 14:11
 **/
@Data
@ApiModel("企业用户积分明细Vo")
public class IntegralUserListVo implements Serializable {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("积分数量")
    private Integer integral;
    @ApiModelProperty("获取途径")
    private String reason;
    @ApiModelProperty("用户类型 1：企业用户，2：个人用户")
    private Integer userType;

}
