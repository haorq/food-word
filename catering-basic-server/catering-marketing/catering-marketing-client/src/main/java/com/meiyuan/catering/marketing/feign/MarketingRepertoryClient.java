package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.service.CateringMarketingRepertoryService;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryEventSoldVo;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName MarketingRepertoryClient
 * @Description
 * @Author gz
 * @Date 2020/5/20 10:29
 * @Version 1.1
 */
@Service
public class MarketingRepertoryClient {
    @Autowired
    private CateringMarketingRepertoryService repertoryService;

    /**
     * 获取秒杀已售数量
     *
     * @param mGoodsId
     * @return
     */
    public Result<Integer> getSoldOutFormGoodsId(Long mGoodsId) {
        return Result.succ(repertoryService.getSoldOutFormGoodsId(mGoodsId));
    }

    /**
     * 同步秒杀库存--数据库
     *
     * @param mGoodsId
     * @param number
     * @param isLess         库存加／减
     * @param seckillEventId 秒杀场次ID
     * @return
     */
    public Result syncSeckillInventory(Long mGoodsId, Integer number, boolean isLess, Long seckillEventId) {
        repertoryService.syncSeckillInventory(mGoodsId, number, isLess, seckillEventId);
        return Result.succ();
    }


    /**
     * 描述: 通过ofId 查询商品库存
     *
     * @param ofId
     * @param userType
     * @return com.meiyuan.catering.core.util.Result<java.util.Map   <   java.lang.Long ,   java.lang.Integer>>
     * @author zengzhangni
     * @date 2020/7/13 14:18
     * @since v1.2.0
     */
    public Result<Map<String, Integer>> getInventoryByOfId(Long ofId, Integer userType) {
        return Result.succ(repertoryService.getInventoryByOfId(ofId, userType));
    }

    /**
     * 查询商品的销售情况
     *
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 18:48
     * @return: {@link List<MarketingRepertoryGoodsSoldVo>}
     **/
    public Result<List<MarketingRepertoryGoodsSoldVo>> marketingProjectedCostCount(Long marketingId) {
        return Result.succ(repertoryService.marketingProjectedCostCount(marketingId));
    }

    /**
     * 查询指定营销商品集合售出的数量
     *
     * @param mGoodsIdSet 营销商品ID集合
     * @author: GongJunZheng
     * @date: 2020/8/11 15:20
     * @return: {@link Result<List<MarketingRepertoryEventSoldVo>>}
     * @version V1.3.0
     **/
    public Result<List<MarketingRepertoryEventSoldVo>> soldBySeckillMarketingGoodsIds(Set<Long> mGoodsIdSet) {
        return Result.succ(repertoryService.soldBySeckillMarketingGoodsIds(mGoodsIdSet));
    }

    /**
     * 查询指定营销商品集合、指定场次售出的数量
     *
     * @param eventId      秒杀场次ID集合
     * @param mGoodsIdList 营销商品ID集合
     * @author: GongJunZheng
     * @date: 2020/8/12 18:28
     * @return: {@link List<MarketingRepertoryEventSoldVo>}
     * @version V1.3.0
     **/
    public Result<List<MarketingRepertoryEventSoldVo>> soldByEventMarketingGoodsId(Long eventId, List<Long> mGoodsIdList) {
        return Result.succ(repertoryService.soldByEventMarketingGoodsId(eventId, mGoodsIdList));
    }

    public Result<Integer> getInventoryByOfMGoodsId(Long mGoodsId, Long seckillEventId) {
        return Result.succ(repertoryService.getInventoryByOfMGoodsId(mGoodsId, seckillEventId));
    }
}
