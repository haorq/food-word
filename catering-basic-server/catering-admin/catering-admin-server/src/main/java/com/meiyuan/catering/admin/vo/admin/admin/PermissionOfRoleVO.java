package com.meiyuan.catering.admin.vo.admin.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionOfRoleVO {

    @ApiModelProperty("app端角色列表")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    List<Long> appPermissionIds = new ArrayList<>();

    @ApiModelProperty("商户pc端角色列表")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    List<Long> pcPermissionsIds = new ArrayList<>();
}
