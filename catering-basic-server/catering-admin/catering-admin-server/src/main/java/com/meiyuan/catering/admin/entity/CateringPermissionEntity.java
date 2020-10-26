package com.meiyuan.catering.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("catering_common_permission")
public class CateringPermissionEntity extends IdEntity {

    @TableField("id")
    private Long id;
    // 菜单名称
    private String menuName;
    // 父菜单id
    private Long parentId;
    // 菜单链接
    private String url;
    // 菜单类型适用类型：0:商户pc，1：app，2：平台
    private int type;
    // 标志
    private String code;

    private Integer sort;

    private String level;
    /**
     * 删除标记
     */
    @TableField(value = "is_del")
    private Boolean del;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}
