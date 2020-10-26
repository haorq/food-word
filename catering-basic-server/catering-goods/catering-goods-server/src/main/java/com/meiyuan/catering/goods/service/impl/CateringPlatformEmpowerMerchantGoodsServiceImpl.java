package com.meiyuan.catering.goods.service.impl;

import com.meiyuan.catering.goods.service.CateringPlatformEmpowerMerchantGoodsService;
import com.meiyuan.catering.goods.dao.CateringPlatformEmpowerMerchantGoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 平台授权商户商品表(CateringPlatformEmpowerMerchantGoods)表服务实现类
 *
 * @author wxf
 * @since 2020-03-09 18:15:26
 */
@Service
public class CateringPlatformEmpowerMerchantGoodsServiceImpl implements CateringPlatformEmpowerMerchantGoodsService {
    @Resource
    private CateringPlatformEmpowerMerchantGoodsMapper cateringPlatformEmpowerMerchantGoodsMapper;

    
}