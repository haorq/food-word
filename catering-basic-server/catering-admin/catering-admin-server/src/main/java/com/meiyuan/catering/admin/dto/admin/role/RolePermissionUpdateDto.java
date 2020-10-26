package com.meiyuan.catering.admin.dto.admin.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author MeiTao
 * @Date  2020/3/2 0002 16:01
 */
@Data
@ApiModel("权限变更Dto")
public class RolePermissionUpdateDto {

    @ApiModelProperty(value = "角色id")
    private Long roleId;
    @ApiModelProperty(value = "权限")
    private List<String> permissions;
}
