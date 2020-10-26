package com.meiyuan.catering.user.query.integral;

import com.meiyuan.catering.core.page.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zengzhangni
 * @date 2020/3/18
 */
@Data
@ApiModel("积分活动列表查询DTO")
public class IntegralRuleQueryDTO extends BasePageDTO {

    @ApiModelProperty("积分活动名称")
    private String name;
    @ApiModelProperty("禁用状态，0为正常，1为删除")
    private Integer status;
}
