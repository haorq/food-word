package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.entity.CateringMarketingRepertoryEntity;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryEventSoldVo;
import com.meiyuan.catering.marketing.vo.repertory.MarketingRepertoryGoodsSoldVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 营销库存表(CateringMarketingRepertory)表数据库访问层
 *
 * @author wxf
 * @since 2020-03-10 11:25:05
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Mapper
public interface CateringMarketingRepertoryMapper extends BaseMapper<CateringMarketingRepertoryEntity> {


    /**
     * 扣减库存
     *
     * @param id         ofId或mGoodsId
     * @param number     扣减数量
     * @param ofType     ofType
     * @param activityId
     */
    void updateRepertory(@Param("id") Long id, @Param("number") Integer number, @Param("ofType") Integer ofType, @Param("activityId") Long activityId);

    /**
     * 扣减库存--批量
     *
     * @param ids        ofId或mGoodsId
     * @param number     扣减数量
     * @param activityId
     */
    void updateRepertoryBatch(@Param("ids") List<Long> ids, @Param("number") Integer number, @Param("activityId") Long activityId);

    /**
     * 描述:
     *
     * @param shopId
     * @param userType
     * @return java.util.List<com.meiyuan.catering.marketing.entity.CateringMarketingRepertoryEntity>
     * @author zengzhangni
     * @date 2020/7/13 14:39
     * @since v1.2.0
     */
    List<CateringMarketingRepertoryEntity> getInventoryByOfId(@Param("shopId") Long shopId, @Param("userType") Integer userType);

    /**
     * 查询未销售完毕的商品的预计成本
     *
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 18:48
     * @return: {@link List<MarketingRepertoryGoodsSoldVo>}
     * @since v1.3.0
     **/
    List<MarketingRepertoryGoodsSoldVo> marketingProjectedCostCount(@Param("marketingId") Long marketingId);

    /**
     * 查询指定营销商品集合售出的数量
     *
     * @param mGoodsIdSet 营销商品ID集合
     * @author: GongJunZheng
     * @date: 2020/8/11 15:20
     * @return: {@link List<MarketingRepertoryEventSoldVo>}
     * @version V1.3.0
     **/
    List<MarketingRepertoryEventSoldVo> soldBySeckillMarketingGoodsIds(@Param("mGoodsIdSet") Set<Long> mGoodsIdSet);

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
    List<MarketingRepertoryEventSoldVo> soldByEventMarketingGoodsId(@Param("eventId") Long eventId, @Param("mGoodsIdList") List<Long> mGoodsIdList);

    /**
     * 根据秒杀活动ID集合刷新秒杀商品库存
     *
     * @param seckillIdList 秒杀活动ID集合
     * @author: GongJunZheng
     * @date: 2020/8/12 13:43
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    void refurbishSeckillGoodsRepertory(@Param("seckillIdList") List<Long> seckillIdList);

    /**
     * 方法描述: 通过活动ID移除关联优惠券库存信息<br>
     *
     * @param activityId
     * @author: gz
     * @date: 2020/8/27 13:33
     * @return: {@link int}
     * @version 1.3.0
     **/
    int removeByActivityId(Long activityId);

    /**
     * 方法描述: 通过优惠券规则关联主键ID更新优惠券库存数据<br>
     *
     * @param ticketRuleRecordList
     * @param number
     * @author: gz
     * @date: 2020/8/27 13:34
     * @return: {@link }
     * @version 1.3.0
     **/

    void updateRepertoryByTicketRuleRecordId(@Param("list") List<Long> ticketRuleRecordList, @Param("number") Integer number);


    /**
     * 秒杀更新库存
     *
     * @param mGoodsId          秒杀活动商品ID
     * @param secKillEventId   秒杀场次ID
     * @param numberWithStock   更新库存数量
     * @param numberWithSoldOut 更新已售数量
     * @return int 秒杀更新库存返回的更新条数
     */
    int updateStockByMgoodsIdAndSeckKillEventId(
            @Param("mGoodsId") Long mGoodsId,
            @Param("secKillEventId") Long secKillEventId,
            @Param("numberWithStock") Integer numberWithStock,
            @Param("numberWithSoldOut") Integer numberWithSoldOut);

}
