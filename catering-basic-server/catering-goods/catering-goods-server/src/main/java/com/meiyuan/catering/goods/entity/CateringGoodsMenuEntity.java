package com.meiyuan.catering.goods.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

/**
 * 菜单表(CateringGoodsMenu)实体类
 *
 * @author wxf
 * @since 2020-03-18 18:28:12
 */
@Data
@TableName("catering_goods_menu")
public class CateringGoodsMenuEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 262753099085737775L;
     /**
     * 菜单名称
     */
    @TableField(value = "menu_name")
    private String menuName;
    /**
     * 自动编号 true-自动 false-手动
     */
    @TableField(value = "auto_code")
    private Boolean autoCode;
     /**
     * 菜单编码
     */
    @TableField(value = "menu_code")
    private String menuCode;
     /**
     * 1-早餐 2-午餐 3-晚餐 4-下午茶
     */
    @TableField(value = "menu_type")
    private Integer menuType;
     /**
     * 送达时间
     */
    @TableField(value = "service_time")
    private LocalDate serviceTime;
     /**
     * 菜单图片
     */
    @TableField(value = "menu_picture")
    private String menuPicture;
     /**
     * 1-下架,2-上架
     */
    @TableField(value = "menu_status")
    private Integer menuStatus;
    /**
     * 菜单描述
     */
    @TableField(value = "menu_describe")
    private String menuDescribe;
    /**
     * 上架时间
     */
    @TableField(value = "upper_shelf_time")
    private LocalDate upperShelfTime;
    /**
     * 下架时间
     */
    @TableField(value = "lower_shelf_time")
    private LocalDate lowerShelfTime;
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