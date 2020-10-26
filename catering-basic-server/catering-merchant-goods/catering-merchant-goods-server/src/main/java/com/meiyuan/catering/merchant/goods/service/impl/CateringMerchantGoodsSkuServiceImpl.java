package com.meiyuan.catering.merchant.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.merchant.goods.dao.CateringMerchantGoodsSkuMapper;
import com.meiyuan.catering.merchant.goods.entity.CateringMerchantGoodsSkuEntity;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsExtendService;
import com.meiyuan.catering.merchant.goods.service.CateringMerchantGoodsSkuService;
import com.meiyuan.catering.merchant.goods.vo.MarketingSpecialGoodsMinQuantityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author lhm
 * @version 1.0 2020年7月3日
 */
@Service
public class CateringMerchantGoodsSkuServiceImpl extends ServiceImpl<CateringMerchantGoodsSkuMapper, CateringMerchantGoodsSkuEntity> implements CateringMerchantGoodsSkuService {

    @Autowired
    private CateringMerchantGoodsSkuMapper cateringMerchantGoodsSkuMapper;
    @Autowired
    private CateringMerchantGoodsExtendService merchantGoodsExtendService;

    /**
     * 描述：获取商户sku的最大值
     *
     * @param merchantId
     * @return {@link Integer}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    @Override
    public Long skuCodeMaxInteger(Long merchantGoodsId,Long merchantId) {
        CateringMerchantGoodsSkuEntity entity = cateringMerchantGoodsSkuMapper.maxDbCode(merchantGoodsId);
        if (null == entity) {
            return 0L;
        }
        String skuCode = entity.getSkuCode();
        return Long.valueOf(skuCode.split("SKU" + merchantId)[0]);
    }

    @Override
    public CateringMerchantGoodsSkuEntity getGoodsSkuInfo(Long extendId, String skuCode) {
        return this.baseMapper.getGoodsSkuInfo(extendId, skuCode);
    }

    @Override
    public List<MarketingSpecialGoodsMinQuantityVO> selectGoodsMinQuantity(Long merchantId, Set<String> skuCodeSet) {
        // 先通过merchantId查询商户最新的商品的商家商品扩展表ID集合
        List<Long> goodsExtendIds = merchantGoodsExtendService.listLastIdByMerchantId(merchantId);
        // 再通过商品的商家商品扩展表ID集合查询最新的商品起售数量
        return baseMapper.selectGoodsMinQuantity(goodsExtendIds, skuCodeSet);
    }
}
