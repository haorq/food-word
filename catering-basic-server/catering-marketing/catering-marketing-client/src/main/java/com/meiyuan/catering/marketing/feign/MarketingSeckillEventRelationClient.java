package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.service.CateringMarketingSeckillEventRelationService;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillDetailEventVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventIdsVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventInfoVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/06 16:08
 * @description 秒杀场次关系Client
 **/

@Slf4j
@Service
public class MarketingSeckillEventRelationClient {

    @Autowired
    private CateringMarketingSeckillEventRelationService seckillEventRelationService;

    /**
     * 根据秒杀ID查询场次信息
     *
     * @param seckillId 秒杀ID
     * @author: GongJunZheng
     * @date: 2020/8/6 16:09
     * @return: {@link List<String>}
     **/
    public Result<List<MarketingSeckillDetailEventVO>> selectEventListBySeckillId(Long seckillId) {
        return Result.succ(seckillEventRelationService.selectEventListBySeckillId(seckillId));
    }

    /**
     * 根据场次ID以及指定日期查询秒杀活动ID集合
     *
     * @param dateTime 日期
     * @param eventId  场次ID
     * @param shopIds  店铺ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 18:26
     * @return: {@link Result<List<MarketingSeckillEventInfoVO>>}
     * @version V1.3.0
     **/
    public Result<List<MarketingSeckillEventInfoVO>> selectSeckillIds(LocalDateTime dateTime, Long eventId, List<Long> shopIds) {
        return Result.succ(seckillEventRelationService.selectSeckillIds(dateTime, eventId, shopIds));
    }

    /**
     * 根据秒杀活动ID查询秒杀场次信息
     *
     * @param seckillIdList 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 11:54
     * @return: {@link Result<List< MarketingSeckillEventVO >>}
     * @version V1.3.0
     **/
    public Result<List<MarketingSeckillEventVO>> selectEventList(List<Long> seckillIdList) {
        return Result.succ(seckillEventRelationService.selectEventList(seckillIdList));
    }

    /**
     * 根据场次ID查询指定日期的秒杀活动的营销商品mGoodsId
     *
     * @param eventId  场次ID
     * @param dateTime 日期时间
     * @param shopIds  店铺ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 16:31
     * @return: {@link Result<List<Long>>}
     * @version V1.3.0
     **/
    public Result<List<Long>> selectEventMarketingGoodsIds(LocalDateTime dateTime, Long eventId, List<Long> shopIds) {
        return Result.succ(seckillEventRelationService.selectEventMarketingGoodsIds(dateTime, eventId, shopIds));
    }

    public Result<List<MarketingSeckillEventVO>> selectUnderwayEventList(List<Long> seckillIdList) {
        return Result.succ(seckillEventRelationService.selectUnderwayEventList(seckillIdList));
    }

    public Result<List<Long>> underwayEventIdList(List<Long> seckillEventIds) {
        return Result.succ(seckillEventRelationService.underwayEventIdList(seckillEventIds));
    }

    public Result<MarketingSeckillEventVO> queryByEventId(Long seckillEventId) {
        return Result.succ(seckillEventRelationService.queryByEventId(seckillEventId));
    }

    /**
    * 根据秒杀活动ID集合查询场次ID集合
    * @param seckillIds 秒杀ID集合
    * @author: GongJunZheng
    * @date: 2020/9/2 11:39
    * @return: {@link List<MarketingSeckillEventIdsVO>}
    * @version V1.4.0
    **/
    public Result<List<MarketingSeckillEventIdsVO>> selectEventIdsBySeckillIds(Set<Long> seckillIds) {
        return Result.succ(seckillEventRelationService.selectEventIdsBySeckillIds(seckillIds));
    }

}
