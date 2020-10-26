package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.page.PageData;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.dto.marketing.MarketingSeckillGrouponPageQueryDTO;
import com.meiyuan.catering.marketing.service.CateringMarketingService;
import com.meiyuan.catering.marketing.vo.marketing.MarketingSeckillGrouponPageQueryVO;
import com.meiyuan.catering.marketing.vo.pullnew.MarketingPullNewCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author GongJunZheng
 * @date 2020/08/05 14:08
 * @description 统一的营销活动Client
 **/

@Service
public class MarketingClient {

    @Autowired
    private CateringMarketingService marketingService;

    public PageData<MarketingSeckillGrouponPageQueryVO> pageQuery(MarketingSeckillGrouponPageQueryDTO dto) {
        return marketingService.pageQuery(dto);
    }

    public Map<Long, MarketingPullNewCountVO> pullNewCount(List<Long> marketingIds) {
        return marketingService.pullNewCount(marketingIds);
    }

    /**
    * 门店被删除，同步设置该门店的活动为冻结状态
    * @param shopId 门店ID
    * @author: GongJunZheng
    * @date: 2020/8/12 16:29
    * @return: {@link Result}
    * @version V1.3.0
    **/
    public Result shopDelSync(Long shopId) {
        marketingService.shopDelSync(shopId);
        return Result.succ();
    }




}
