package com.meiyuan.catering.merchant.goods.util;

import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.generator.CodeGenerator;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wxf
 * @date 2020/3/16 18:08
 * @description 简单描述
 **/
@Component
@Slf4j
public class MerchantGoodsUtils {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.MERCHAT_GOODS_CODE_KEY, area = JetcacheAreas.MERCHANT_AREA))
    private AdvancedCache merchantGoodsCodeCache;

    /**
     * @param merchantCode 商户号
     * @return string 商品编码
     * @description 商品编码 SP+商户号+redis自增
     * @author yaozou
     * @date 2020/3/18 16:30
     * @since v1.0.0
     */
    public String goodsCode(Long merchantCode, Integer dbMaxCodeInteger) {
        String merchantId = String.valueOf(merchantCode);
        // SP+商户号
        String prefix = CodeGenerator.goodsSpuCodePrefix(merchantId);
        Long num = merchantGoodsCodeCache.increment(prefix, 1);
        return prefix + num;
    }

    /**
     * 描述：sku编码
     *
     * @param merchantCode
     * @param dbMaxCodeInteger
     * @return {@link String}
     * @author lhm
     * @date 2020/7/6
     * @version 1.2.0
     **/
    public String skuCode(String merchantCode, Long dbMaxCodeInteger) {
        // sku+商户号
        String prefix = CodeGenerator.skuCodePrefix(merchantCode);
        Long num = merchantGoodsCodeCache.increment(prefix, merchantGoodsCodeCache.hasKey(merchantCode) ? 1 : dbMaxCodeInteger + 1);
        return prefix + num;
    }


}
