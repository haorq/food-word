package com.meiyuan.catering.merchant.goods.fegin;


import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopDiscountGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDiscountDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSkuService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class ShopGoodsSkuClient {

    @Resource
    private CateringShopGoodsSkuService shopGoodsSkuService;


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
    public Result<Map<String, Integer>> getRemainStock(Long merchantId, Long shopId, Long categoryId) {
        return Result.succ(shopGoodsSkuService.getRemainStock(merchantId, shopId, categoryId));
    }


    /**
     * 描述:获取单个商品库存
     *
     * @param merchantId
     * @param shopId
     * @param skuCode
     * @return com.meiyuan.catering.core.util.Result<java.lang.Integer>
     * @author zengzhangni
     * @date 2020/7/14 10:23
     * @since v1.2.0
     */
    public Result<Integer> getRemainStockBySku(Long merchantId, Long shopId, String skuCode) {
        return Result.succ(shopGoodsSkuService.getRemainStockBySku(merchantId, shopId, skuCode));
    }

    /**
     * 方法描述 : 查询包含折扣商品的店铺
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    public Result<List<Long>> listShopDiscountGoods(ShopDiscountGoodsDTO dto) {
        return Result.succ(shopGoodsSkuService.listShopDiscountGoods(dto));
    }

    /**
     * 方法描述 : 查询店铺是否有商品
     *
     * @param dto 店铺ids:若为空则表示查询所有有商品的店铺
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    public Result<List<Long>> listShopHaveGoods(ShopDiscountGoodsDTO dto) {
        return Result.succ(shopGoodsSkuService.listShopHaveGoods(dto));
    }


    /**
     * 根据SKU和门店ID查询SKU名称和对应的商品名称 auth:lh version:1.3.0
     *
     * @param sku
     * @param shopId
     * @return
     */
    public Result<ShopSkuDTO> queryBySkuAndShopId(String sku, Long shopId) {
        return Result.succ(shopGoodsSkuService.queryBySkuAndShopId(sku, shopId));
    }

    /**
     * 方法描述 : 查询店铺最低折扣
     *
     * @param dto 店铺ids，用户类型
     * @Author: MeiTao
     * @Date: 2020/8/3 0003 14:45
     * @return: java.util.List<java.lang.Long>
     * @Since version-1.3.0
     */
    public Result<List<ShopGoodsDiscountDTO>> listShopGoodsDiscount(ShopDiscountGoodsDTO dto){
        return Result.succ(shopGoodsSkuService.listShopGoodsDiscount(dto));
    }

    /**
    * 校验商品是否在销售菜单里面
    * @param merchantId 商户ID
    * @param shopId 门店ID
    * @param skuMap 商品SKU信息Map
    * @author: GongJunZheng
    * @date: 2020/8/27 13:57
    * @return: void
    * @version V1.3.0
    **/
    public void verifyMarketingGoods(Long merchantId, Long shopId, Map<String, Long> skuMap) {
        shopGoodsSkuService.verifyMarketingGoods(merchantId, shopId, skuMap);
    }

    /**
    * 校验营销特价商品信息
    * @param shopId 门店ID
    * @param goodsSkuList 商品SKU编码集合
    * @author: GongJunZheng
    * @date: 2020/9/3 10:07
    * @return: {@link List<CateringShopGoodsSkuEntity>}
    * @version V1.4.0
    **/
    public Result<List<CateringShopGoodsSkuEntity>> verifySpecialGoods(Long shopId, List<String> goodsSkuList) {
        return Result.succ(shopGoodsSkuService.verifySpecialGoods(shopId,goodsSkuList));
    }
}
