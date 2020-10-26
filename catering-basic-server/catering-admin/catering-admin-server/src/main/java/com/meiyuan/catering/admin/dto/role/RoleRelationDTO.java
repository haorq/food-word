package com.meiyuan.catering.admin.dto.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author amdin
 */
@Data
public class RoleRelationDTO {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("员工id")
    private Long employeeId;
    @ApiModelProperty("角色id")
    private Long roleId;
    @ApiModelProperty("角色名称")
    private String roleName;
}
