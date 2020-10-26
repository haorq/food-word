package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 类目商品关联表(CateringGoodsCategoryRelation)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:36:05
 */
@Data
@Accessors(chain = true)
@TableName("catering_goods_category_relation")
public class CateringGoodsCategoryRelationEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -76178896380525155L;
    /**
     * 分类id
     */
    @TableField(value = "category_id")
    private Long categoryId;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;

    /**
     * 排序
     */
    @TableField(value = "sort")
    private Integer sort;

    public CateringGoodsCategoryRelationEntity(Long categoryId, Long goodsId, Integer sort) {
        super();
        this.categoryId = categoryId;
        this.goodsId = goodsId;
        this.sort = sort;
    }
    public CateringGoodsCategoryRelationEntity() {
    }
}