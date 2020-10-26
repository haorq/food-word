package com.meiyuan.catering.marketing.service;

import com.meiyuan.catering.core.util.Result;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 秒杀拉新用户明细服务层
 **/

public interface CateringMarketingPullNewService {

    /**
     * 查询营销活动实际拉新人数
     * @param marketingId 营销活动ID
     * @author: GongJunZheng
     * @date: 2020/8/6 19:05
     * @return: {@link Result<Integer>}
     **/
    Integer marketingPullCount(Long marketingId);

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
    void insertPullNew(Long ofId, Integer ofType, Long userId, Long orderId);

    /**
     * 订单退款，逻辑删除拉新数据
     * @param orderId 订单ID
     * @author: GongJunZheng
     * @date: 2020/8/14 10:09
     * @return: {@link Boolean}
     * @version V1.3.0
     **/
    void delPullNew(Long orderId);
}
