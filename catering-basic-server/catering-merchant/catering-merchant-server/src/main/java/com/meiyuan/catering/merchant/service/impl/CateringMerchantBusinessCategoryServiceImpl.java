package com.meiyuan.catering.merchant.service.impl;
import com.meiyuan.catering.merchant.dao.CateringMerchantBusinessCategoryMapper;
import com.meiyuan.catering.merchant.service.CateringMerchantBusinessCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商户经营分类表(CateringMerchantBusinessCategory)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
public class CateringMerchantBusinessCategoryServiceImpl implements CateringMerchantBusinessCategoryService {
    @Resource
    private CateringMerchantBusinessCategoryMapper cateringMerchantBusinessCategoryMapper;

    
}