package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDiscountDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringShopGoodsSkuService extends IService<CateringShopGoodsSkuEntity> {

    /**
     * 描述:通过商户id+门店id+分类id 查询商品库存
     *
     * @param merchantId
     * @param shopId
     * @param categoryId
     * @return
     * @author zengzhangni
     * @date 2020/7/14 10:09
     * @since v1.2.0
     */
    Map<String, Integer> getRemainStock(Long merchantId, Long shopId, Long categoryId);


    /**
     * describe:根据门店ID和SKU编码集合查询SKU列表
     *
     * @param shopId      门店ID
     * @param skuCodeList 商品SKU编码集合
     * @return
     * @author lh
     * @version 1.2.0
     */
    List<CateringShopGoodsSkuEntity> list(Long shopId, List<String> skuCodeList);

    /**
     * 描述:获取单个商品库存
     *
     * @param merchantId
     * @param shopId
     * @param skuCode
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/7/14 10:22
     * @since v1.2.0
     */
    Integer getRemainStockBySku(Long merchantId, Long shopId, String skuCode);

    /**
     * 方法描述 : 查询包含折扣商品的店铺
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<Long> listShopDiscountGoods(ShopDiscountGoodsDTO dto);


    /**
     * 根据SKU和门店ID查询SKU名称和对应的商品名称 auth:lh version:1.3.0
     *
     * @param sku
     * @param shopId
     * @return
     */
    ShopSkuDTO queryBySkuAndShopId(String sku, Long shopId);

    /**
     * 方法描述 : 查询店铺是否有商品
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<Long> listShopHaveGoods(ShopDiscountGoodsDTO dto);

    /**
     * 方法描述 : 查询店铺最低折扣
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<ShopGoodsDiscountDTO> listShopGoodsDiscount(ShopDiscountGoodsDTO dto);

    /**
     * 校验商品是否在销售菜单里面
     *
     * @param merchantId 商户ID
     * @param shopId     门店ID
     * @param skuMap     商品SKU信息Map
     * @author: GongJunZheng
     * @date: 2020/8/27 13:57
     * @return: void
     * @version V1.3.0
     **/
    void verifyMarketingGoods(Long merchantId, Long shopId, Map<String, Long> skuMap);


    /**
     * 批量更新商品库存（下单扣库存，取消订单恢复库存）
     *
     * @param shopId
     * @param skuMap
     * @return 成功更新库存结果条数，为0则更新失败
     */
    void batchUpdateSkuStock(Long shopId, ConcurrentHashMap<String/* 订单商品sku */, Integer/* 订单商品数量，下单为负，取消为正 */> skuMap);

    /**
     * 校验营销特价商品信息
     * @param shopId 门店ID
     * @param goodsSkuList 商品SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/9/3 10:07
     * @return: {@link List<CateringShopGoodsSkuEntity>}
     * @version V1.4.0
     **/
    List<CateringShopGoodsSkuEntity> verifySpecialGoods(Long shopId, List<String> goodsSkuList);
}
