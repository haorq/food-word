package com.meiyuan.catering.merchant.goods.fegin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.dto.es.ShopGoodsSku;
import com.meiyuan.catering.core.dto.es.ShopGoodsStatusMap;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.goods.dto.goods.GoodsUpOrDownDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantCategoryOrGoodsSortDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantGoodsSkuDTO;
import com.meiyuan.catering.merchant.goods.dto.merchant.MerchantSortDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopGoodsDTO;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsRelationEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsRelationService;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSkuService;
import com.meiyuan.catering.merchant.goods.service.CateringShopGoodsSpuService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lhm
 * @date 2020/7/8
 * @description
 **/
@Service
public class ShopGoodsClient {
    @Autowired
    private CateringShopGoodsSpuService shopGoodsSpuService;
    @Autowired
    private CateringShopGoodsSkuService shopGoodsSkuService;
    @Autowired
    private CateringShopGoodsRelationService shopGoodsRelationService;

    /**
     * 描述：门店商品上下架
     *
     * @param downDTO
     * @return {@link Result}
     * @author lhm
     * @date 2020/7/8
     * @version 1.2.0
     **/
    public Result<Boolean> upOrDown(GoodsUpOrDownDTO downDTO) {
        return Result.succ(shopGoodsSpuService.updateGoodsStatus(downDTO.getUpOrDownStatus(), downDTO.getGoodsId(), downDTO.getShopId()));
    }


    /**
     * 批量更新SKU库存，库存增量传正，库存加；传负，库存减
     *
     * @param shopId
     * @param skuMap
     * @return
     * @author lh
     * @version 1.2.0
     */
    public Result batchUpdateSkuStock(Long shopId, ConcurrentHashMap<String/* 订单商品sku */, Integer/* 订单商品数量 */> skuMap) {
        // 这里返回的list是设置了库存的商品列表
        List<CateringShopGoodsSkuEntity> list = shopGoodsSkuService.list(shopId, Lists.newArrayList(skuMap.keySet()));
        if (CollectionUtils.isEmpty(list)) {
            return Result.succ();
        }
        List<String> orderGoodsSkuList = list.stream().map(CateringShopGoodsSkuEntity::getSkuCode).collect(Collectors.toList());

        Iterator<String> iterator = skuMap.keySet().iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            if (orderGoodsSkuList.contains(next)){
                continue;
            }
            // 移除没有设置库存的商品
            iterator.remove();
        }

        if (skuMap.size() <= 0) {
            return Result.succ();
        }
        shopGoodsSkuService.batchUpdateSkuStock(shopId, skuMap);
        return Result.succ();
    }

    /**
     * 方法描述: 保存门店商品关联数据<br>
     *
     * @param merchantId
     * @author: gz
     * @date: 2020/7/14 10:26
     * @return: {@link }
     * @version 1.2.0
     **/
    public void saveShopGoods(Long merchantId, List<Long> removeShopIds, Long menuId) {
        shopGoodsSpuService.saveShopGoods(merchantId, removeShopIds, menuId);
    }

    /**
     * 描述：修改门店库存 价格
     *
     * @param merchantGoodsSkuDTOList
     * @return {@link }
     * @author lhm
     * @date 2020/7/15
     * @version 1.2.0
     **/
    public void updateSku(List<Long> ids, List<MerchantGoodsSkuDTO> merchantGoodsSkuDTOList) {
        shopGoodsSpuService.updateShopSku(ids, merchantGoodsSkuDTOList);
    }

    public Boolean saveShopGoodsTwo(Long merchantId, List<String> deleteSkuList, List<String> addSkuList, List<Long> deleteShopList,
                                    List<Long> addShopIdList, List<Long> oldShopIdList, List<String> oldSkuList) {
        return shopGoodsSpuService.saveShopGoodsTwo(merchantId, deleteSkuList, addSkuList, deleteShopList, addShopIdList, oldShopIdList, oldSkuList);
    }

    /**
     * 描述：根据商品和商户 获取门店spu表信息
     *
     * @param goodsId
     * @param merchantId
     * @return {@link ShopGoodsDTO}
     * @author lhm
     * @date 2020/7/15
     * @version 1.2.0
     **/
    public List<Long> getShopGoodsId(Long goodsId, Long merchantId) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(CateringShopGoodsSpuEntity::getId)
                .eq(CateringShopGoodsSpuEntity::getGoodsId, goodsId)
                .eq(CateringShopGoodsSpuEntity::getMerchantId, merchantId)
                .eq(CateringShopGoodsSpuEntity::getDel, false);
        return shopGoodsSpuService.listObjs(queryWrapper, obj -> Long.valueOf(obj.toString()));
    }


    /**
     * 方法描述   刷新门店es
     *
     * @param
     * @author: lhm
     * @date: 2020/7/18 11:17
     * @return: {@link }
     * @version 1.1.0
     **/
    public void pushMerchantGoodsEs(Long goodsId, Long merchantId) {
        shopGoodsSpuService.pushMerchantGoodsEs(goodsId, merchantId);
    }


    /**
     * 方法描述   获取门店上下架状态
     *
     * @param goodsId
     * @param shopId
     * @author: lhm
     * @date: 2020/7/25 9:48
     * @return: {@link }
     * @version 1.1.0
     **/
    public Integer getShopStatus(Long goodsId, Long shopId) {
        return shopGoodsSpuService.getShopStatus(goodsId, shopId);
    }

    public List<ShopGoodsStatusMap> getShopGoodsStatus(Set<Long> shopIds) {
        return shopGoodsSpuService.getShopGoodsStatus(shopIds);
    }

    public List<ShopGoodsSku> getShopGoodsSkus(Set<Long> shopIds) {
        return shopGoodsSpuService.getShopGoodsSkus(shopIds);
    }


    /**
     * 方法描述   门店商品排序
     * @author: lhm
     * @date: 2020/8/24 13:55
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> updateGoodsSort(MerchantCategoryOrGoodsSortDTO dto) {
        return Result.succ(shopGoodsSpuService.updateGoodsSort(dto));
    }

    /**
     * 方法描述   app--商品置顶
     * @author: lhm
     * @date: 2020/9/1 13:43
     * @param dto
     * @return: {@link }
     * @version 1.3.0
     **/
    public Result<Boolean> updateSortToUp(MerchantSortDTO dto) {
        return Result.succ(shopGoodsSpuService.updateSortToUp(dto));
    }

    /**
     * 根据品牌ID以及商品ID查询门店商品SPU信息
     *
     * @param merchantId 品牌ID
     * @param goodsId    商品ID
     * @author: GongJunZheng
     * @date: 2020/8/14 14:20
     * @return: {@link List<CateringShopGoodsSpuEntity>}
     * @version V1.3.0
     **/
    public Result<List<CateringShopGoodsSpuEntity>> selectShopGoodsSpu(Long merchantId, Long goodsId) {
        return Result.succ(shopGoodsSpuService.selectShopGoodsSpu(merchantId, goodsId));
    }



    /**
     * 方法描述   获取goodsId
     * @author: lhm
     * @date: 2020/9/3 11:32
     * @param shopIds
     * @return: {@link }
     * @version 1.3.0
     **/
    public List<Long> getShopGoodsId(Set<Long>  shopIds) {
        QueryWrapper<CateringShopGoodsSpuEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .select(CateringShopGoodsSpuEntity::getGoodsId)
                .in(CateringShopGoodsSpuEntity::getShopId, shopIds)
                .eq(CateringShopGoodsSpuEntity::getDel, false);
        return shopGoodsSpuService.listObjs(queryWrapper, obj -> Long.valueOf(obj.toString()));
    }

    public Result<String> pushGoodsEsToDb() {
        return Result.succ(shopGoodsSpuService.pushGoodsEsToDb());
    }

    public Boolean updateCategory() {
        return shopGoodsSpuService.updateCategory();
    }
}
