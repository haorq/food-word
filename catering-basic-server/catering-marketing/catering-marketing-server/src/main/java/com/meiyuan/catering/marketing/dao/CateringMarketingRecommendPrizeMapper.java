package com.meiyuan.catering.marketing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeQueryDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeTicketDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendRecordDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRecommendPrizeEntity;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
@Mapper
public interface CateringMarketingRecommendPrizeMapper extends BaseMapper<CateringMarketingRecommendPrizeEntity> {
    /**
     * 方法描述: 推荐有奖分页查询<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:02
     * @param page
     * @param queryDTO
     * @return: {@link IPage< RecommendPrizeListVO>}
     * @version 1.1.1
     **/
    IPage<RecommendPrizeListVO> listPage(Page page, @Param("queryDTO") RecommendPrizeQueryDTO queryDTO);

    /**
     * 保存推荐有奖活动关联的优惠券
     *
     * @param ticketDTO
     * @return
     */
    int insertTicket(@Param("ticketDTO") RecommendPrizeTicketDTO ticketDTO);

    /**
     * 删除推荐有奖活动关联的优惠券
     *
     * @param recommendPrizeId 推荐有奖活动ID
     * @return
     */
    int deleteTicket(@Param("recommendPrizeId") Long recommendPrizeId);

    /**
     * 查询推荐有奖活动关联的优惠券
     *
     * @param recommendPrizeId 推荐有奖活动ID
     * @return
     */
    RecommendPrizeTicketDTO getTicket(Long recommendPrizeId);

    /**
     * 方法描述: 统计时间范围内推荐有奖活动数量<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:05
     * @param begin
     * @param end
     * @param recommendCondition
     * @param ignoredRecommendPrizeId
     * @return: {@link long}
     * @version 1.1.1
     **/
    long countByTime(@Param("begin") LocalDateTime begin, @Param("end") LocalDateTime end,
                     @Param("recommendCondition") Integer recommendCondition, @Param("ignoredRecommendPrizeId") Long ignoredRecommendPrizeId);

    /**
     * 根据推荐条件获取推荐有奖活动
     *
     * @param recommendCondition 推荐条件枚举
     * @return
     */
    CateringMarketingRecommendPrizeEntity getByRecommendCondition(Integer recommendCondition);

    /**
     * 保存推荐记录
     *
     * @param recommendRecordDTO
     * @return
     */
    int insertRecommendRecord(@Param("recommendRecordDTO") RecommendRecordDTO recommendRecordDTO);

    /**
     * 根据被推荐人ID获取推荐记录
     *
     * @param referralId
     * @return
     */
    RecommendRecordDTO getRecommendRecordByReferralId(Long referralId);
}
