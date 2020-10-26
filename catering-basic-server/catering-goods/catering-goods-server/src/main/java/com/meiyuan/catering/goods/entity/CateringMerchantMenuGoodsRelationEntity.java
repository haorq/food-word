package com.meiyuan.catering.goods.entity;;

import java.io.Serializable;
import com.meiyuan.catering.core.entity.IdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 商户菜单商品关联表(CateringMerchantMenuGoodsRelation)实体类
 *
 * @author wxf
 * @since 2020-03-18 18:30:45
 */
@Data
@TableName("catering_merchant_menu_goods_relation")
public class CateringMerchantMenuGoodsRelationEntity extends IdEntity
implements Serializable {
    private static final long serialVersionUID = 360556686058624492L;
     /**
     * 商户id
     */
    @TableField(value = "merchant_id")
    private Long merchantId;
     /**
     * 菜单id
     */
    @TableField(value = "menu_id")
    private Long menuId;
     /**
     * 商品id
     */
    @TableField(value = "goods_id")
    private Long goodsId;
     /**
     * sku编码
     */
    @TableField(value = "sku_code")
    private String skuCode;
     /**
     * 1-下架,2-上架
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 固定商家还是全部商家 1-所有商家 2-指定商家
     */
    @TableField(value = "fixed_or_all")
    private Integer fixedOrAll;
    /**
     * 数据绑定类型 1- 商品推送 2-菜单推送 3-菜单绑定菜品
     */
    @TableField(value = "data_bind_type")
    private Integer dataBindType;

}