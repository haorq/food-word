package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 标签商品关联表(CateringGoodsLabelRelation)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:36:55
 */
@Data
@TableName("catering_goods_label_relation")
public class CateringGoodsLabelRelationEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = 292385816252528717L;
    /**
     * 标签id
     */
    @TableField(value = "label_id")
    private Long labelId;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
}
