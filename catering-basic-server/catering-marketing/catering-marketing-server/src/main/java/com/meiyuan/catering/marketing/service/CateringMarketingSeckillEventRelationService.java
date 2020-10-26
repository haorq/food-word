package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventRelationEntity;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillDetailEventVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventIdsVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventInfoVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 平台秒杀活动场次与门店秒杀关系服务层
 **/

public interface CateringMarketingSeckillEventRelationService {

    /**
     * 根据秒杀ID删除秒杀场次关系信息
     *
     * @param seckillId 秒杀ID
     * @author: GongJunZheng
     * @date: 2020/8/6 13:51
     * @return: {@link Integer}
     **/
    int delBySeckillId(Long seckillId);

    /**
     * 批量保存秒杀场次关系信息
     *
     * @param seckillEventRelationList 秒杀场次关系信息集合
     * @author: GongJunZheng
     * @date: 2020/8/6 13:51
     * @return: {@link Boolean}
     **/
    Boolean saveBathList(List<CateringMarketingSeckillEventRelationEntity> seckillEventRelationList);

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
    List<MarketingSeckillEventInfoVO> selectSeckillIds(LocalDateTime dateTime, Long eventId, List<Long> shopIds);

    /**
     * 根据秒杀ID查询场次时间信息
     *
     * @param seckillId 秒杀活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 16:30
     * @return: {@link List<String>}
     **/
    List<MarketingSeckillDetailEventVO> selectEventListBySeckillId(Long seckillId);

    /**
     * 根据秒杀活动ID查询秒杀场次信息
     *
     * @param seckillIdList 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 11:54
     * @return: {@link Result <List< MarketingSeckillEventVO >>}
     * @version V1.3.0
     **/
    List<MarketingSeckillEventVO> selectEventList(List<Long> seckillIdList);

    /**
     * 根据场次ID查询秒杀活动的营销商品mGoodsId
     *
     * @param eventId  场次ID
     * @param dateTime 日期时间
     * @param shopIds  店铺ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 16:31
     * @return: {@link Result<List<Long>>}
     * @version V1.3.0
     **/
    List<Long> selectEventMarketingGoodsIds(LocalDateTime dateTime, Long eventId, List<Long> shopIds);

    /**
     * 根据秒杀活动ID集合查询秒杀场次关系信息
     *
     * @param seckillIdList 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/8/12 11:52
     * @return: {@link CateringMarketingSeckillEventRelationEntity}
     * @version V1.3.0
     **/
    List<CateringMarketingSeckillEventRelationEntity> selectSeckillEventList(List<Long> seckillIdList);


    /**
     * 描述:批量查询进行中的场次
     *
     * @param seckillIdList
     * @return java.util.List<com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO>
     * @author zengzhangni
     * @date 2020/8/13 15:30
     * @since v1.3.0
     */
    List<MarketingSeckillEventVO> selectUnderwayEventList(List<Long> seckillIdList);

    /**
     * 描述:过滤进行中的场次
     *
     * @param seckillEventIds
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/8/13 15:31
     * @since v1.3.0
     */
    List<Long> underwayEventIdList(List<Long> seckillEventIds);

    /**
     * 描述:通过场次id查询场次信息
     *
     * @param seckillEventId
     * @return com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO
     * @author zengzhangni
     * @date 2020/8/13 15:35
     * @since v1.3.0
     */
    MarketingSeckillEventVO queryByEventId(Long seckillEventId);

    /**
    * 根据秒杀活动ID集合查询场次ID集合
    * @param seckillIds 秒杀活动ID集合
    * @author: GongJunZheng
    * @date: 2020/9/2 10:39
    * @return: {@link List<MarketingSeckillEventIdsVO>}
    * @version V1.4.0
    **/
    List<MarketingSeckillEventIdsVO> selectEventIdsBySeckillIds(Set<Long> seckillIds);
}
