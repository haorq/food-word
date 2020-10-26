package com.meiyuan.catering.admin.service.marketing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeDTO;
import com.meiyuan.catering.marketing.dto.recommend.RecommendPrizeQueryDTO;
import com.meiyuan.catering.marketing.feign.MarketingRecommendPrizeClient;
import com.meiyuan.catering.marketing.service.CateringMarketingRecommendPrizeService;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeDetailVO;
import com.meiyuan.catering.marketing.vo.recommend.RecommendPrizeListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author luohuan
 * @date 2020/3/20
 **/
@Service
public class AdminMarketingRecommendPrizeService {
    @Autowired
    private MarketingRecommendPrizeClient recommendPrizeClient;

    /**
     * 分页查询推荐有奖活动
     *
     * @param queryDTO
     * @return
     */
    public Result<IPage<RecommendPrizeListVO>> listPage(RecommendPrizeQueryDTO queryDTO) {
        return recommendPrizeClient.listPage(queryDTO);
    }

    /**
     * 新增推荐有奖活动
     *
     * @param prizeDTO
     */
    public Result create(RecommendPrizeDTO prizeDTO) {
        return recommendPrizeClient.create(prizeDTO);
    }

    /**
     * 更新推荐有奖活动
     *
     * @param prizeDTO
     */
    public Result update(RecommendPrizeDTO prizeDTO) {
        return recommendPrizeClient.update(prizeDTO);
    }

    /**
     * 删除推荐有奖活动
     *
     * @param id
     */
    public Result delete(Long id) {
        return recommendPrizeClient.delete(id);
    }

    /**
     * 推荐有奖活动详情
     *
     * @param id
     * @return
     */
    public Result<RecommendPrizeDetailVO> detail(Long id) {
        return recommendPrizeClient.detail(id);
    }

}
