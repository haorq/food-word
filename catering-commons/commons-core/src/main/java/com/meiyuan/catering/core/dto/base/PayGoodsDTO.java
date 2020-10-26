package com.meiyuan.catering.core.dto.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 */
@Data
public class PayGoodsDTO implements Serializable {
    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 营销商品表主键id
     */
    private Long mGoodsId;

    /**
     * 关联ID
     */
    private Long ofId;

    /**
     * 关联ID归属类型:1-秒杀；2-拼团；3-团购；4-优惠券；
     */
    private Integer ofType;

    /**
     * 商品类型（1--菜单商品，2--普通商品，3--拼单商品，4--秒杀商品；5--团购商品；6—菜单拼单）
     */
    private Integer goodsType;
    /**
     * 商品SKU编码
     */
    private String goodsSkuCode;
    /**
     * 商品购买数量
     */
    private Integer quantity;


}
