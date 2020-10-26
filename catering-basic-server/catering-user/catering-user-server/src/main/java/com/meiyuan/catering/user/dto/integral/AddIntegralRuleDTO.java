package com.meiyuan.catering.user.dto.integral;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zengzhangni
 * @date 2020/3/16
 */
@Data
@ApiModel("积分规则添加编辑DTO")
public class AddIntegralRuleDTO implements Serializable {

    @ApiModelProperty("id id=null:新增 反之修改")
    private Long id;
    @ApiModelProperty("积分规则名称")
    private String name;
    @ApiModelProperty("积分")
    private Integer integral;
    @ApiModelProperty("禁用状态，0为正常，1为删除")
    private Integer status;
}
