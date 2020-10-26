package com.meiyuan.catering.user.vo.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @since 2020-03-18
 */
@Data
@ApiModel("积分规则")
public class IntegralRuleListVo implements Serializable {

    @ApiModelProperty("积分")
    private Integer integral;
    @ApiModelProperty("积分规则名称")
    private String ruleName;

}
