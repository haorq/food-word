package com.meiyuan.catering.core.dto.es;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zengzhangni
 * @date 2020/8/6 18:14
 * @since v1.1.0
 */
@Data
public class EsDataMap {

    /**
     * 门店信息
     */
    Map<String, ShopInfoDTO> shopMap;
    /**
     * 门店状态信息
     */
    Map<Long, Map<Long, ShopGoodsStatusMap>> shopGoodsStatus;
    /**
     * 门店sku信息
     */
    Map<Long, Map<Long, List<ShopGoodsSku>>> shopGoodsSkus;
    /**
     * 门店特价商品信息
     */
    Map<Long, Map<String, MarketingSpecialSku>> shopSpecialSkuMap;


    public ShopInfoDTO getShop(String key) {
        if (shopMap == null) {
            return null;
        }
        return shopMap.get(key);
    }

    public Map<Long, ShopGoodsStatusMap> getShopGoods(Long key) {
        if (shopGoodsStatus == null) {
            return null;
        }
        return shopGoodsStatus.get(key);
    }

    public Map<Long, List<ShopGoodsSku>> getShopSku(Long key) {
        if (shopGoodsSkus == null) {
            return null;
        }
        return shopGoodsSkus.get(key);
    }

    public Map<String, MarketingSpecialSku> getShopSpecialSku(Long key) {
        if(shopSpecialSkuMap == null) {
            return null;
        }
        return shopSpecialSkuMap.get(key);
    }
}
