package com.meiyuan.catering.user.vo.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zengzhangni
 * @date 2020/3/25
 **/
@Data
@ApiModel("积分列表Vo")
public class IntegralListVo implements Serializable {

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    @ApiModelProperty("积分数量")
    private Integer integral;
    @ApiModelProperty("获取途径")
    private String reason;
    @ApiModelProperty("类型 1：增加，2：减少")
    private Integer type;
    @ApiModelProperty("月 yyyy-MM")
    private String month;

}
