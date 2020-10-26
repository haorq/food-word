package com.meiyuan.catering.admin.dto.admin.admin;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("角色编辑新增dto")
public class CommonRoleParamsDTO implements Serializable {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty(name = "门店id或者-1(-1代表平台)", hidden = true)
    private Long subjectId;
}
