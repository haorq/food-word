package com.meiyuan.catering.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品属性表(CateringGoodsProperty)实体类
 *
 * @author wxf
 * @since 2020-03-09 17:36:55
 */
@Data
@TableName("catering_goods_property")
public class CateringGoodsPropertyEntity extends IdEntity
        implements Serializable {
    private static final long serialVersionUID = -17255576900277193L;
    /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
    /**
     * 商品编码
     */
    @TableField(value = "spu_code")
    private String spuCode;
    /**
     * 属性类型id
     */
    @TableField(value = "property_type_id")
    private Long propertyTypeId;
    /**
     * 属性类型名称
     */
    @TableField(value = "property_type_name")
    private String propertyTypeName;
    /**
     * 属性值 json格式
     */
    @TableField(value = "property_value")
    private String propertyValue;
}