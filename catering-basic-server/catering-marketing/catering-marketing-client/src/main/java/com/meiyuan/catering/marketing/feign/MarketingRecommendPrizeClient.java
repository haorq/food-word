package com.meiyuan.catering.marketing.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeQueryDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendRecordDTO;
import com.meiyuan.catering.marketing.entity.CateringMarketingRecommendPrizeEntity;
import com.meiyuan.catering.marketing.enums.MarketingRecommendPrizeConditionEnum;
import com.meiyuan.catering.marketing.service.CateringMarketingRecommendPrizeService;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeDetailVO;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luohuan
 * @date 2020/5/19
 **/
@Service("marketingRecommendPrizeClient")
public class MarketingRecommendPrizeClient {
    @Autowired
    private CateringMarketingRecommendPrizeService recommendPrizeService;

    /**
     * 分页查询推荐有奖活动
     *
     * @param queryDTO 查询条件
     * @return Result<IPage < RecommendPrizeListVO>>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<IPage<RecommendPrizeListVO>> listPage(RecommendPrizeQueryDTO queryDTO) {
        return Result.succ(recommendPrizeService.listPage(queryDTO));
    }

    /**
     * 创建推荐有奖活动
     *
     * @param prizeDTO 推荐有奖DTO
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result create(RecommendPrizeDTO prizeDTO) {
        recommendPrizeService.create(prizeDTO);
        return Result.succ();
    }

    /**
     * 更新推荐有奖活动
     *
     * @param prizeDTO 推荐有奖DTO
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result update(RecommendPrizeDTO prizeDTO) {
        recommendPrizeService.update(prizeDTO);
        return Result.succ();
    }

    /**
     * 删除推荐有奖活动
     *
     * @param id 推荐有奖ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result delete(Long id) {
        recommendPrizeService.delete(id);
        return Result.succ();
    }

    /**
     * 推荐有奖活动详情
     *
     * @param id 推荐有奖ID
     * @return Result<RecommendPrizeDetailVO>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<RecommendPrizeDetailVO> detail(Long id) {
        return Result.succ(recommendPrizeService.detail(id));
    }

    /**
     * 根据推荐条件获取推荐有奖活动
     *
     * @param prizeConditionEnum 推荐条件枚举
     * @return Result<CateringMarketingRecommendPrizeEntity>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<CateringMarketingRecommendPrizeEntity> getByRecommendCondition(MarketingRecommendPrizeConditionEnum prizeConditionEnum) {
        return Result.succ(recommendPrizeService.getByRecommendCondition(prizeConditionEnum));
    }

    /**
     * 保存推荐记录
     *
     * @param referrerId 推荐人ID
     * @param referralId 被推荐人ID
     * @return Result
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result createRecommendRecord(Long referrerId, Long referralId) {
        recommendPrizeService.createRecommendRecord(referrerId, referralId);
        return Result.succ();
    }

    /**
     * 根据被推荐人ID获取推荐记录
     *
     * @param referralId 被推荐人ID
     * @return Result<RecommendRecordDTO>
     * @author luohuan
     * @date 2020/5/19
     * @since v1.0.0
     */
    public Result<RecommendRecordDTO> getRecommendRecordByReferralId(Long referralId) {
        return Result.succ(recommendPrizeService.getRecommendRecordByReferralId(referralId));
    }
}
