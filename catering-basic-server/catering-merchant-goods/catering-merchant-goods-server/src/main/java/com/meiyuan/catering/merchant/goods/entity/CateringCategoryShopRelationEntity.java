package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_category_shop_relation")
public class CateringCategoryShopRelationEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 2282165725898319046L;
    /**
     * 分类id
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 门店id
     */
    @TableField("shop_id")
    private Long shopId;

    /**
     * 1：平台推送  2：商家推送 3：门店自创
     */
    @TableField("category_add_type")
    private Integer categoryAddType;

    /**
     * 门店分类排序
     */
    @TableField("sort")
    private Integer sort;


}
