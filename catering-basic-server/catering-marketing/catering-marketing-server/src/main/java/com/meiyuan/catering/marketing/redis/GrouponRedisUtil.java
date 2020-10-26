package com.meiyuan.catering.marketing.redis;

import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author luohuan
 * @date 2020/4/9
 **/
@Slf4j
@Component
public class GrouponRedisUtil {

    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.GROUPON_SOLDOUT, area = JetcacheAreas.GOODS_AREA))
    private AdvancedCache grouponSoldOutCache;

    /**
     * 增加团购已售数量
     *
     * @param grouponGoodsId
     * @param number
     */
    public void incrementSoldOut(Long grouponGoodsId, Integer number) {
        grouponSoldOutCache.increment(String.valueOf(grouponGoodsId), number);
    }

    /**
     * 扣减团购已售数量
     *
     * @param grouponGoodsId
     * @param number
     */
    public void decrementSoldOut(Long grouponGoodsId, Integer number) {
        grouponSoldOutCache.decrement(String.valueOf(grouponGoodsId), number);
    }

    /**
     * 获取团购已售数量
     *
     * @param grouponGoodsId
     * @return
     */
    public int getSoldOut(Long grouponGoodsId) {
        Long soldOut = grouponSoldOutCache.increment(String.valueOf(grouponGoodsId),0);
        return soldOut.intValue();
    }



}
