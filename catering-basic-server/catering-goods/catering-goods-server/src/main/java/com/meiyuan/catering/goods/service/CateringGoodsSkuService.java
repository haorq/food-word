package com.meiyuan.catering.goods.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.goods.entity.CateringGoodsSkuEntity;

/**
 * 商品规格(SKU)表(CateringGoodsSku)服务层
 *
 * @author wxf
 * @since 2020-03-09 18:05:09
 */
public interface CateringGoodsSkuService extends IService<CateringGoodsSkuEntity> {
    /**
     * sku最大值
     *
     * @author: wxf
     * @date: 2020/6/22 11:57
     * @param merchant 商户id
     * @return: {@link Integer}
     * @version 1.1.1
     **/
    Integer skuCodeMaxInteger(String merchant);
}