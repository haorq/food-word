package com.meiyuan.catering.merchant.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
public interface CateringMerchantGoodsSkuService extends IService<CateringMerchantGoodsSkuEntity> {

    /**
     * 描述：获取商户sku的最大值
     * @author lhm
     * @date 2020/7/6
     * @param merchantGoodsId
     * @param merchantId
     * @return {@link Integer}
     * @version 1.2.0
     **/
    Long skuCodeMaxInteger(Long merchantGoodsId,Long merchantId);

    /**
    * 根据商品扩展表id以及SKU编码查询SKU信息
    * @param extendId 商品扩展表id
    * @param skuCode SKU编码
    * @author: GongJunZheng
    * @date: 2020/8/27 14:38
    * @return: {@link }
    * @version V1.3.0
    **/
    CateringMerchantGoodsSkuEntity getGoodsSkuInfo(Long extendId, String skuCode);

    /**
     * 根据商户ID、SKU编码集合查询商品的起售数量
     * @param merchantId 商户ID
     * @param skuCodeSet SKU编码集合
     * @author: GongJunZheng
     * @date: 2020/9/10 9:27
     * @return: {@link List<MarketingSpecialGoodsMinQuantityVO>}
     * @version V1.4.0
     **/
    List<MarketingSpecialGoodsMinQuantityVO> selectGoodsMinQuantity(Long merchantId, Set<String> skuCodeSet);
}
