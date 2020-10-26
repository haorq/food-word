package com.meiyuan.catering.marketing.service;


import com.meiyuan.catering.marketing.enums.MarketingOfTypeEnum;

import java.util.List;

/**
 * 购买记录表(CateringMarketingRecord)服务层
 *
 * @author gz
 * @since 2020-03-10 11:30:31
 */
public interface CateringMarketingRecordService  {
    /**
     * 同步秒杀用户的已购数量
     * @param mGoodsId 活动商品主键id
     * @param userId 用户id
     * @param number 数量
     * @param isLess 是否减数量 -- true：减数量；false--加数量
     */
    void syncSeckillUserHaveGought(Long mGoodsId,Long userId,Integer number,boolean isLess);

    /**
     * 方法描述: 记录用户领取优惠券数量<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:06
     * @param ticketId 优惠券id
     * @param userId 用户id
     * @param number 数量
     * @return: {@link }
     * @version 1.1.1
     **/
    void recordTicketUserGet(Long ticketId,Long userId, Integer number);

    /**
     * 方法描述: 获取用户已领取记录<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:06
     * @param userId 用户id
     * @param ofId
     * @param ofTypeEnum 类型枚举
     * @return: {@link Integer}
     * @version 1.1.1
     **/
    Integer getUserRecord(Long userId,Long ofId, MarketingOfTypeEnum ofTypeEnum);

    /**
     * 方法描述: 记录用户领取优惠券数量-批量<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:06
     * @param ticketIds 优惠券ids
     * @param userId 用户id
     * @param number 数量
     * @return: {@link }
     * @version 1.1.1
     **/
    void recordTicketUserGetBatch(List<Long> ticketIds,Long userId,Integer number);

    /**
     * 方法描述: 记录用户领取数量-批量用户id<br>
     *
     * @author: gz
     * @date: 2020/6/23 14:07
     * @param userIds 用户ids
     * @param ticketId 优惠券id/优惠券参与的活动ID
     * @param number 数量
     * @return: {@link }
     * @version 1.1.1
     **/
    void recordTicketUserGetBatchUserId(List<Long> userIds,Long ticketId,Integer number);
}