package com.meiyuan.catering.admin.vo.admin.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BriefRoleVO {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
}
