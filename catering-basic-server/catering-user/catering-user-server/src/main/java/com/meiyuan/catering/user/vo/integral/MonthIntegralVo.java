package com.meiyuan.catering.user.vo.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/3/25
 */
@Data
@ApiModel("积分明细Vo")
public class MonthIntegralVo {

    @ApiModelProperty("月 yyyy-MM")
    private String month;
    @ApiModelProperty("获取")
    private Integer gain;
    @ApiModelProperty("使用")
    private Integer employ;
    @ApiModelProperty("记录")
    private List<IntegralListVo> list;

}
