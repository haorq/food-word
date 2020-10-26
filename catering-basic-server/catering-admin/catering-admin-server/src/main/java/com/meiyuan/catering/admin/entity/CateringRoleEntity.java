package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("catering_common_role")
public class CateringRoleEntity extends IdEntity {

    @TableField("id")
    private Long id;
    // 店铺id或平台id（平台用-1表示）
    private Long subjectId;
    // 角色名称
    private String roleName;
    // 是否默认0：否1：是(默认不能删除）
    private Integer defaultFlag;
    // 角色描述
    private String remark;
    // 删除标记：0：未删除，1：已删除
    private Integer isDel;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
