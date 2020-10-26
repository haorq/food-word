package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.service.CateringMarketingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName MarketingRecordClient
 * @Description
 * @Author gz
 * @Date 2020/5/19 16:34
 * @Version 1.1
 */
@Service
public class MarketingRecordClient {

    @Autowired
    private CateringMarketingRecordService recordService;
    /**
     * 同步秒杀用户的已购数量
     * @param mGoodsId 活动商品主键id
     * @param userId 用户id
     * @param number 数量
     * @param isLess 是否减数量 -- true：减数量；false--加数量
     */
    public Result syncSeckillUserHaveGought(Long mGoodsId, Long userId, Integer number, boolean isLess){
        recordService.syncSeckillUserHaveGought(mGoodsId,userId,number,isLess);
        return Result.succ();
    }
}
