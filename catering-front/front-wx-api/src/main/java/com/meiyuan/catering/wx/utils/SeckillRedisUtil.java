package com.meiyuan.catering.wx.utils;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.meiyuan.catering.core.redis.JetcacheAreas;
import com.meiyuan.catering.core.redis.JetcacheNames;
import com.meiyuan.catering.marketing.dto.seckill.SeckillGoodsDetailsDTO;
import com.meiyuan.catering.marketing.feign.MarketingRepertoryClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillClient;
import com.meiyuan.marsh.jetcache.AdvancedCache;
import com.meiyuan.marsh.jetcache.anno.AdvancedCreateCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @ClassName SeckillRedisUtil
 * @Description
 * @Author gz
 * @Date 2020/3/26 11:17
 * @Version 1.1
 */
@Slf4j
@Component
public class SeckillRedisUtil {
    /**
     * 秒杀商品库存
     */
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_INVENTORY, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache cache;
    /**
     * 秒杀商品详情
     */
    @CreateCache(area = JetcacheAreas.MARKETING_AREA, name = JetcacheNames.SECKILL_GOODS)
    private Cache<String, SeckillGoodsDetailsDTO> goodsCache;
    /**
     * 用户已购缓存
     */
    @AdvancedCreateCache(@CreateCache(area = JetcacheAreas.MARKETING_AREA, name = JetcacheNames.SECKILL_USER_HAVE_BOUGHT))
    private AdvancedCache userHaveBoughtCache;
    /**
     * 秒杀销量
     */
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_SOLDOUT, area = JetcacheAreas.GOODS_AREA))
    private AdvancedCache seckillSoldOutCache;
    /**
     * 秒杀购物车标识
     */
    @AdvancedCreateCache(@CreateCache(name = JetcacheNames.SECKILL_CART, area = JetcacheAreas.MARKETING_AREA))
    private AdvancedCache seckillCartCache;
    @Autowired
    private MarketingSeckillClient seckillClient;
    @Autowired
    private MarketingRepertoryClient repertoryClient;



    /**
     * 在redis中进行真正意义上的秒杀
     *
     * @param seckillGoodsId
     * @param number         -1
     */
    public void handleInRedis(Long seckillGoodsId, Integer number, Long userId) {
        // 扣减库存
        cache.increment(String.valueOf(seckillGoodsId), number);
        // 累计用户已购数量
        String boughtKey = seckillGoodsId + ":" + userId;
        // 购物车标识，用户下单删除此标识
        seckillCartCache.set(boughtKey, Boolean.TRUE);
        // 增加秒杀商品销量
        seckillSoldOutCache.decrement(seckillGoodsId.toString(), number);
    }


    /**
     * 恢复秒杀库存
     *
     * @param seckillGoodsId 活动商品表主键id
     * @param number         数量
     * @param userId         用户id
     * @param order          标识是否需要处理数据库的库存
     * @param pay            标识是否支付
     * @param seckillEventId 秒杀场次 v1.3.0
     */
    public void asynRestoreInvertory(Long seckillGoodsId, Integer number, Long userId, Boolean order, Boolean pay, Long seckillEventId) {
        // 恢复用户的已购数量
        if (order) {
            seckillClient.syncSeckillInvertory(seckillGoodsId, userId, number, false, pay,seckillEventId);
        }
    }



    /**
     * 获取秒杀销量
     *
     * @param seckillGoodsId
     * @return
     */
    public Integer getSeckillSoldOut(Long seckillGoodsId) {

        return repertoryClient.getSoldOutFormGoodsId(seckillGoodsId).getData();
    }

    /**
     * 获取秒杀商品用户购物车标识
     *
     * @param seckillGoodsId
     * @param userId
     * @return
     */
    public boolean getSeckillCartFlag(Long seckillGoodsId, Long userId) {
        String boughtKey = seckillGoodsId + ":" + userId;
        return seckillCartCache.hasKey(boughtKey);
    }


    /**
     * 失效时间计算
     *
     * @param endTime
     * @return
     */
    private long seckillTtl(LocalDateTime endTime) {
        return Duration.between(LocalDateTime.now(), endTime).getSeconds();
    }

}
