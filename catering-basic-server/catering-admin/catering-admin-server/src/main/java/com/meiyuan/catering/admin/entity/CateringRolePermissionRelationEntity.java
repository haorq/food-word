package com.meiyuan.catering.admin.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("catering_common_role_permission_relation")
@AllArgsConstructor
@NoArgsConstructor
public class CateringRolePermissionRelationEntity extends IdEntity {

    @TableField("id")
    private Long id;

    private Long roleId;

    private Long permissionId;
}
