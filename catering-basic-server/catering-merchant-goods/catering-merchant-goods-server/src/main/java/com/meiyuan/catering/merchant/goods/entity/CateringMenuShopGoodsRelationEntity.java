package com.meiyuan.catering.merchant.goods.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meiyuan.catering.core.entity.IdEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Data
@TableName("catering_menu_shop_goods_relation")
public class CateringMenuShopGoodsRelationEntity extends IdEntity implements Serializable {

    private static final long serialVersionUID = -4901507575466689529L;

    @TableField("menu_id")
    private Long menuId;

    @TableField("shop_id")
    private Long shopId;

    @TableField("shop_name")
    private String shopName;

}
