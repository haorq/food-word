package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.marketing.dao.CateringMarketingTicketActivityShopMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingTicketActivityShopEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingTicketActivityShopService;
import org.springframework.stereotype.Service;

/**
 * @ClassName CateringMarketingTicketActivityShopServiceImpl
 * @Description
 * @Author gz
 * @Date 2020/8/5 17:28
 * @Version 1.3.0
 */
@Service
public class CateringMarketingTicketActivityShopServiceImpl extends ServiceImpl<CateringMarketingTicketActivityShopMapper, CateringMarketingTicketActivityShopEntity> implements CateringMarketingTicketActivityShopService {
    @Override
    public int removeByActivityId(Long activityId) {
        return this.baseMapper.removeByActivityId(activityId);
    }

    @Override
    public int updateShopTicketStatus(Long shopId, Long activityId) {
        return this.baseMapper.updateShopTicketStatus(shopId,activityId);
    }
}
