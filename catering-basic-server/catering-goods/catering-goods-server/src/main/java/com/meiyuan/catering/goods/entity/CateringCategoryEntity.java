package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类目表(CateringCategory)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:35:00
 */
@Data
@TableName("catering_category")
public class CateringCategoryEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 614215618628198495L;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
    /**
     * 默认分类 1-新增 2-默认
     */
    @TableField(value = "default_category")
    private Integer defaultCategory;
    /**
     * 类目名称
     */
    @TableField(value = "category_name")
    private String categoryName;
    /**
     * 类目关键字，以JSON数组格式
     */
    @TableField(value = "keywords")
    private String keywords;
    /**
     * 父类目ID 0表示一级分类
     */
    @TableField(value = "parent_id")
    private Long parentId;
    /**
     * 类目图标
     */
    @TableField(value = "icon_url")
    private String iconUrl;
    /**
     * 类目图片
     */
    @TableField(value = "category_picture")
    private String categoryPicture;
    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;
    /**
     * 层级id
     */
    @TableField(value = "hierarchy_id")
    private String hierarchyId;
    /**
     * 类目描述
     */
    @TableField(value = "category_describe")
    private String categoryDescribe;
    /**
     * 0-否 1-是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 修改人
     */
    @TableField(value = "update_by", fill = FieldFill.UPDATE)
    private Long updateBy;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;
}