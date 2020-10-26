package com.meiyuan.catering.admin.vo.admin.admin;

import lombok.Data;

@Data
public class PermissionVO {

    private Long roleId;

    private Long permissionId;

    private Long parentId;

    private Integer type;
}
