package com.meiyuan.catering.admin.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("catering_common_subject_role_relation")
public class CateringSubjectRoleRelationEntity extends IdEntity {

    @TableField("id")
    private Long id;
    // 员工id或admin表id
    private Long subjectId;
    // 角色Id
    private Long roleId;
}
