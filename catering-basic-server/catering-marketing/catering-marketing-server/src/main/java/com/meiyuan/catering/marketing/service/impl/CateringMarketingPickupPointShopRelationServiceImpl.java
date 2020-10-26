package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.marketing.dao.CateringMarketingPickupPointShopRelationMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingPickupPointShopRelationEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingPickupPointShopRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author mt
 * @date 2020/3/19
 **/
@Service("cateringMarketingPickupPointShopRelationService")
public class CateringMarketingPickupPointShopRelationServiceImpl extends ServiceImpl<CateringMarketingPickupPointShopRelationMapper, CateringMarketingPickupPointShopRelationEntity>
        implements CateringMarketingPickupPointShopRelationService {
    @Resource
    private CateringMarketingPickupPointShopRelationMapper pickupPointShopRelationMapper;

}
