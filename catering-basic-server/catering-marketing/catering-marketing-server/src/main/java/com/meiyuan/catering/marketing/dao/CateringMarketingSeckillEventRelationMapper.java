package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.entity.CateringMarketingSeckillEventRelationEntity;
import com.meiyuan.catering.marketing.vo.seckill.MarketingSeckillDetailEventVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventIdsVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventInfoVO;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author GongJunZheng
 * @date 2020/08/04 15:08
 * @description 平台秒杀活动场次与门店秒杀关系表(catering_marketing_seckill_event_relation)数据库访问层
 **/

@Mapper
public interface CateringMarketingSeckillEventRelationMapper extends BaseMapper<CateringMarketingSeckillEventRelationEntity> {

    /**
    * 根据秒杀ID删除秒杀场次关系信息
    * @param seckillId 秒杀活动ID
    * @author: GongJunZheng
    * @date: 2020/8/28 14:28
    * @return: {@link int}
    * @version V1.3.0
    **/
    int deleteBySeckillId(@Param("seckillId") Long seckillId);

    /**
    * 根据场次ID以及指定日期查询秒杀活动ID集合
    * @param dateTime 日期
    * @param eventId 场次ID
    * @param shopIds 门店ID集合
    * @author: GongJunZheng
    * @date: 2020/8/28 14:29
    * @return: {@link List<MarketingSeckillEventInfoVO>}
    * @version V1.3.0
    **/
    List<MarketingSeckillEventInfoVO> selectSeckillIds(@Param("dateTime") LocalDateTime dateTime,
                                                       @Param("eventId") Long eventId,
                                                       @Param("shopIds") List<Long> shopIds);

    /**
     * 根据秒杀ID查询场次信息
     *
     * @param seckillId 秒杀ID
     * @author: GongJunZheng
     * @date: 2020/8/6 16:19
     * @return: {@link List<String>}
     **/
    List<MarketingSeckillDetailEventVO> selectEventListBySeckillId(@Param("seckillId") Long seckillId);

    /**
     * 根据秒杀活动ID查询秒杀场次信息
     *
     * @param seckillIdList 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/8/10 11:54
     * @return: {@link List< MarketingSeckillEventVO >}
     * @version V1.3.0
     **/
    List<MarketingSeckillEventVO> selectEventList(@Param("seckillIdList") List<Long> seckillIdList);

    /**
     * 根据场次ID查询秒杀活动的营销商品mGoodsId
     *
     * @param eventId  场次ID
     * @param dateTime 日期时间
     * @author: GongJunZheng
     * @date: 2020/8/10 16:31
     * @return: {@link Result <List<Long>>}
     * @version V1.3.0
     **/
    List<Long> selectEventMarketingGoodsIds(@Param("dateTime") LocalDateTime dateTime,
                                    @Param("eventId") Long eventId);


    /**
     * 描述:批量查询进行中的场次
     *
     * @param seckillIdList
     * @return java.util.List<com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO>
     * @author zengzhangni
     * @date 2020/8/13 15:32
     * @since v1.3.0
     */
    List<MarketingSeckillEventVO> selectUnderwayEventList(@Param("seckillIdList") List<Long> seckillIdList);

    /**
     * 描述:过滤进行中的场次
     *
     * @param seckillEventIds
     * @return java.util.List<java.lang.Long>
     * @author zengzhangni
     * @date 2020/8/13 15:32
     * @since v1.3.0
     */
    List<Long> underwayEventIdList(@Param("seckillEventIds") List<Long> seckillEventIds);

    /**
     * 描述:通过场次id查询场次信息
     *
     * @param seckillEventId
     * @return com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO
     * @author zengzhangni
     * @date 2020/8/13 15:31
     * @since v1.3.0
     */
    MarketingSeckillEventVO queryByEventId(@Param("seckillEventId") Long seckillEventId);

    /**
     * 根据秒杀活动ID集合查询场次ID集合
     * @param seckillIds 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/9/2 10:39
     * @return: {@link List<MarketingSeckillEventIdsVO>}
     * @version V1.4.0
     **/
    List<MarketingSeckillEventIdsVO> selectEventIdsBySeckillIds(@Param("seckillIds") Set<Long> seckillIds);
}
