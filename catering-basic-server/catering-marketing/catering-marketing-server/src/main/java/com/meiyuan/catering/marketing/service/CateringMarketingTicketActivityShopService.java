package com.meiyuan.catering.marketing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketActivityShopEntity;

/**
 * @ClassName CateringMarketingTicketActivityShopService
 * @Description
 * @Author gz
 * @Date 2020/8/5 17:13
 * @Version 1.3.0
 */
public interface CateringMarketingTicketActivityShopService extends IService<CateringMarketingTicketActivityShopEntity> {

    /**
     * 方法描述: 通过活动id删除关联门店信息<br>
     *
     * @author: gz
     * @date: 2020/8/5 18:22
     * @param activityId
     * @return: {@link int}
     * @version 1.3.0
     **/
    int removeByActivityId(Long activityId);

    /**
     * 冻结门店优惠券活动
     * @param shopId
     * @param activityId
     * @return
     */
    int updateShopTicketStatus(Long shopId,Long activityId);
}
