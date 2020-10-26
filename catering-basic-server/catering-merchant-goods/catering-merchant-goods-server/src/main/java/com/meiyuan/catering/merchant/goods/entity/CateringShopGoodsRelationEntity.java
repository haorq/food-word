package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;
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
@TableName("catering_shop_goods_relation")
public class CateringShopGoodsRelationEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = -3524137842929229114L;
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

    @TableField(value = "shop_id")
    private Long shopId;


}