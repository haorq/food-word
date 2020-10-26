package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_menu_goods_relation")
public class CateringMenuGoodsRelationEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = 2154689739770181996L;

    @TableField("menu_id")
    private Long menuId;

    @TableField("goods_id")
    private Long goodsId;

    @TableField("sku_code")
    private String skuCode;

}
