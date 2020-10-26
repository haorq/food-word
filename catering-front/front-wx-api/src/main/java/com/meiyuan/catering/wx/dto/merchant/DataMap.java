package com.meiyuan.catering.wx.dto.merchant;

import lombok.Data;

import java.util.Map;

/**
 * @author zengzhangni
 * @date 2020/8/3 11:45
 * @since v1.3.0
 * <p>
 * 秒杀分类--7(分类选中数量)
 * \
 * \--秒杀商品1--4(商品选中数量)
 * \      \
 * \      \--skuCode--4(sku选中数量)
 * \
 * \--秒杀商品2--3(商品选中数量)
 * \
 * \--skuCode--3(sku选中数量)
 * <p>
 * 我的分类--4(分类选中数量)
 * \
 * \--普通商品1--3(商品选中数量)
 * \      \
 * \      \--skuCode_001--1(sku选中数量)
 * \      \--skuCode_002--2(sku选中数量)
 * \
 * \--普通商品2--1(商品选中数量)
 * \
 * \--skuCode_004--1(sku选中数量)
 */
@Data
public class DataMap {

    /**
     * 分类 选中数量--(前端分类选中展示)
     */
    Map<Long, Integer> categoryMap;
    /**
     * 商品 选中数量--(前端分类选中展示)
     */
    Map<Long, Integer> goodsMap;
    /**
     * 普通商品sku选中数量--(前端起售判断条件)
     */
    Map<String, Integer> ordinarySkuMap;
    /**
     * 秒杀商品sku选中数量--(前端起售判断条件)
     */
    Map<String, Integer> seckillSkuMap;
    /**
     * 商品sku库存
     */
    Map<String, Integer> inventoryMap;
    /**
     * 秒杀商品库存
     */
    Map<String, Integer> seckillInventoryMap;

    public Integer getCategoryValue(Long categoryId) {
        if (categoryMap == null) {
            return null;
        }
        return categoryMap.get(categoryId);
    }

    public Integer getGoodsValue(Long goodsId) {
        if (goodsMap == null) {
            return null;
        }
        return goodsMap.get(goodsId);
    }

    public Integer getOrdinarySkuValue(String skuCode) {
        if (ordinarySkuMap == null) {
            return null;
        }
        return ordinarySkuMap.get(skuCode);
    }

    public Integer getSeckillSkuValue(String skuCode) {
        if (seckillSkuMap == null) {
            return null;
        }
        return seckillSkuMap.get(skuCode);
    }

    public Integer getInventoryMapValue(String skuCode) {
        return inventoryMap.get(skuCode);
    }

    public Integer getSeckillInventoryValue(String key) {
        return seckillInventoryMap.get(key);
    }

}
