package com.meiyuan.catering.merchant.goods.dto.goods;

import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.entity.CateringShopGoodsSpuEntity;
import lombok.Data;

import java.util.List;

/**
 * @ClassName MerchantShopGoodsDTO
 * @Description
 * @Author gz
 * @Date 2020/7/24 11:00
 * @Version 1.2.0
 */
@Data
public class MerchantShopGoodsDTO {

    private List<CateringShopGoodsSkuEntity> skuEntityList;

    private List<CateringShopGoodsSpuEntity> spuEntityList;

}
