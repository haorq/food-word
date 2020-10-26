package com.meiyuan.catering.merchant.goods.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.merchant.goods.dto.es.ShopGoodsSkuDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDiscountDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Mapper
public interface CateringShopGoodsSkuMapper extends BaseMapper<CateringShopGoodsSkuEntity> {


    /**
     * 描述:通过商户id+门店id+分类id 查询商品库存
     *
     * @param merchantId
     * @param shopId
     * @param categoryId
     * @return java.util.List<com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity>
     * @author zengzhangni
     * @date 2020/7/14 10:07
     * @since v1.2.0
     */
    List<CateringShopGoodsSkuEntity> getRemainStock(@Param("merchantId") Long merchantId, @Param("shopId") Long shopId, @Param("categoryId") Long categoryId);


    /**
     * 描述:获取单个商品库存
     *
     * @param merchantId
     * @param shopId
     * @param skuCode
     * @return java.lang.Integer
     * @author zengzhangni
     * @date 2020/7/14 10:20
     * @since v1.2.0
     */
    Integer getRemainStockBySku(@Param("merchantId") Long merchantId, @Param("shopId") Long shopId, @Param("skuCode") String skuCode);


    /**
     * 方法描述 : 查询包含折扣商品的店铺
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<Long> listShopDiscountGoods(@Param("dto") ShopDiscountGoodsDTO dto);


    /**
     * 根据SKU和门店ID查询SKU名称和对应的商品名称 auth:lh version:1.3.0
     *
     * @param sku
     * @param shopId
     * @return
     */
    ShopSkuDTO queryBySkuAndShopId(@Param("sku") String sku, @Param("shopId") Long shopId);

    /**
     * 方法描述 : 查询店铺是否有商品
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<Long> listShopHaveGoods(@Param("dto") ShopDiscountGoodsDTO dto);

    /**
     * 方法描述 : 查询店铺最低折扣
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    List<ShopGoodsDiscountDTO> listShopGoodsDiscount(@Param("dto") ShopDiscountGoodsDTO dto);


    /**
     * 更新库存
     *
     * @param shopId
     * @param skuCode
     * @param count   下单负，取消订单正
     * @return
     */
    int updateSkuStock(@Param("shopId") Long shopId, @Param("skuCode") String skuCode, @Param("count") Integer count);

    /**
     * 校验营销特价商品信息
     * @param shopId 门店ID
     * @param goodsSkuList 商品SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/9/3 10:07
     * @return: {@link List<CateringShopGoodsSkuEntity>}
     * @version V1.4.0
     **/
    List<CateringShopGoodsSkuEntity> verifySpecialGoods(@Param("shopId") Long shopId, @Param("goodsSkuList") List<String> goodsSkuList);
}
