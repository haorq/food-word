package com.meiyuan.catering.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品扩展属性表(CateringGoodsExtendProperty)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:36:32
 */
@Data
@TableName("catering_goods_extend_property")
public class CateringGoodsExtendPropertyEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -92963120211306627L;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 商品编号
     */
    @TableField(value = "spu_code")
    private String spuCode;
    /**
     * 字段名
     */
    @TableField(value = "goods_extend_property_field")
    private String goodsExtendPropertyField;
    /**
     * 字段值
     */
    @TableField(value = "goods_extend_property_value")
    private String goodsExtendPropertyValue;
    /**
     * 1-不启用,2-启用
     */
    @TableField(value = "field_status")
    private Integer fieldStatus;
}