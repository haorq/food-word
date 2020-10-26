package com.meiyuan.catering.merchant.goods.es;

import lombok.Data;

/**
 * @author lhm
 * @date 2020/7/9
 * @description 简单描述
 **/
@Data
public class GoodsShopMenuGoodsDTO {
    private Long id;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 门店id
     */
    private Long shopId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 1-下架,2-上架
     */
    private Integer status;
    /**
     * 固定商家还是全部商家 1-所有商家 2-指定商家
     */
    private Integer fixedOrAll;
    /**
     * 数据绑定类型 1- 商品推送 2-菜单推送 3-菜单绑定菜品
     */
    private Integer dataBindType;
    /**
     * 是否有商品数据
     */
    private Boolean haveGoodsFlag;

    private String goodsName;
}
