package com.meiyuan.catering.marketing.feign;

import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.service.CateringMarketingPullNewService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author GongJunZheng
 * @date 2020/08/06 19:08
 * @description 营销拉新Client
 **/

@Service
public class MarketingPullNewClient {

    @Autowired
    private CateringMarketingPullNewService pullNewService;

    /**
    * 查询营销活动实际拉新人数
    * @param marketingId 营销活动ID
    * @author: GongJunZheng
    * @date: 2020/8/6 19:04
    * @return: {@link Result<Integer>}
    **/
    public Result<Integer> marketingPullCount(Long marketingId) {
        return Result.succ(pullNewService.marketingPullCount(marketingId));
    }

    /**
    * 新增营销活动拉新数据
    * @param ofId 营销活动ID
    * @param ofType 营销活动类型
    * @param userId 用户ID
    * @param orderId 订单ID
    * @author: GongJunZheng
    * @date: 2020/8/14 9:46
    * @return: {@link  Boolean}
    * @version V1.3.0
    **/
    public Result<Boolean> insertPullNew(Long ofId, Integer ofType, Long userId, Long orderId) {
        pullNewService.insertPullNew(ofId, ofType, userId, orderId);
        return Result.succ();
    }

    /**
    * 订单退款，逻辑删除拉新数据
    * @param orderId 订单ID
    * @author: GongJunZheng
    * @date: 2020/8/14 10:09
    * @return: {@link Boolean}
    * @version V1.3.0
    **/
    public Result<Boolean> delPullNew(Long orderId) {
        pullNewService.delPullNew(orderId);
        return Result.succ();
    }

}
