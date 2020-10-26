package com.meiyuan.catering.marketing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品系列扩展表(CateringMarketingGoodsCategory)实体类
 *
 * @author wxf
 * @since 2020-03-10 11:21:41
 */
@Data
@TableName("catering_marketing_goods_category")
public class CateringMarketingGoodsCategoryEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -21317774226424969L;

    /**
     * 营销商品表主键id
     */
    @TableField(value = "m_goods_id")
    private Long mGoodsId;
    @TableField(value = "goods_category_id")
    private Long goodsCategoryId;
    @TableField(value = "goods_category_name")
    private String goodsCategoryName;
    /**
     * 删除标记
     */
    @TableField(value = "is_del",fill = FieldFill.INSERT)
    private Boolean del;
    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 创建人
     */
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 更新人
     */
    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}