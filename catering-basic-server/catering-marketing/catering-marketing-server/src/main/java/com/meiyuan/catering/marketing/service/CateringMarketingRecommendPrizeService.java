package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeQueryDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendRecordDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRecommendPrizeEntity;
import com.meiyuan.catering.marketing.enums.MarketingRecommendPrizeConditionEnum;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeDetailVO;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeListVO;

/**
 * @author luohuan
 * @date 2020/3/19
 **/
public interface CateringMarketingRecommendPrizeService extends IService<CateringMarketingRecommendPrizeEntity> {

    /**
     * 分页查询推荐有奖活动
     *
     * @param queryDTO
     * @return
     */
    IPage<RecommendPrizeListVO> listPage(RecommendPrizeQueryDTO queryDTO);

    /**
     * 创建推荐有奖活动
     *
     * @param prizeDTO
     */
    void create(RecommendPrizeDTO prizeDTO);

    /**
     * 更新推荐有奖活动
     *
     * @param prizeDTO
     */
    void update(RecommendPrizeDTO prizeDTO);

    /**
     * 删除推荐有奖活动
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 推荐有奖活动详情
     *
     * @param id
     * @return
     */
    RecommendPrizeDetailVO detail(Long id);

    /**
     * 根据推荐条件获取推荐有奖活动
     *
     * @param prizeConditionEnum 推荐条件枚举
     * @return
     */
    CateringMarketingRecommendPrizeEntity getByRecommendCondition(MarketingRecommendPrizeConditionEnum prizeConditionEnum);

    /**
     * 保存推荐记录
     *
     * @param referrerId 推荐人ID
     * @param referralId 被推荐人ID
     */
    void createRecommendRecord(Long referrerId, Long referralId);

    /**
     * 根据被推荐人ID获取推荐记录
     *
     * @param referralId 被推荐人ID
     * @return
     */
    RecommendRecordDTO getRecommendRecordByReferralId(Long referralId);
}
