package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.marketing.MarketingSeckillGrouponPageQueryDTO;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.marketing.vo.pullnew.MarketingPullNewCountVO;

import java.util.List;
import java.util.Map;

/**
 * @author GongJunZheng
 * @date 2020/08/05 14:08
 * @description 统一的营销活动Service服务层
 **/

public interface CateringMarketingService {

    /**
     * 营销活动中秒杀/团购同时列表分页查询V1.3.0
     *
     * @param dto 查询条件
     *
     * @return 结果数据
     */
    PageData<MarketingSeckillGrouponPageQueryVO> pageQuery(MarketingSeckillGrouponPageQueryDTO dto);

    /**
     * 统计多个营销活动（秒杀/团购）的拉新结果
     *
     * @param marketingIds 营销活动ID集合
     * @return: {@link Map< Long, MarketingPullNewCountVO>}
     */
    Map<Long, MarketingPullNewCountVO> pullNewCount(List<Long> marketingIds);

    /**
     * 门店被删除，同步设置该门店的活动为冻结状态
     * @param shopId 门店ID
     * @author: GongJunZheng
     * @date: 2020/8/12 16:29
     * @return: {@link Result}
     * @version V1.3.0
     **/
    void shopDelSync(Long shopId);
}
