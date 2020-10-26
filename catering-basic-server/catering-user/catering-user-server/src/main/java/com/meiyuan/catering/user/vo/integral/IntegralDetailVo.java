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
public class IntegralDetailVo {

    @ApiModelProperty("当前积分")
    private Integer integral;
    @ApiModelProperty("月份积分列表")
    private List<MonthIntegralVo> monthIntegrals;

}
