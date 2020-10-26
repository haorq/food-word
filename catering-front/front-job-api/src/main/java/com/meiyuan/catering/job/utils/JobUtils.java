package com.meiyuan.catering.job.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.order.vo.MerchantCountVO;
import org.springframework.stereotype.Component;

/**
 * @author yaoozu
 * @description 微信工具类
 * @date 2020/3/2111:26
 * @since v1.0.0
 */
@Component
public class JobUtils {
    @CreateCache(name = JetcacheNames.MERCHANT_COUNT, area = JetcacheAreas.MERCHANT_AREA)
    private Cache<Long, MerchantCountVO> merchantCountCache;

    /**
     * 功能描述: 从缓存获取商户统计信息
     * @param merchantId
     * @return: com.meiyuan.catering.order.vo.MerchantCountVO
     */
    public MerchantCountVO getMerchantCount(Long merchantId) {
        return merchantCountCache.get(merchantId);
    }

}
