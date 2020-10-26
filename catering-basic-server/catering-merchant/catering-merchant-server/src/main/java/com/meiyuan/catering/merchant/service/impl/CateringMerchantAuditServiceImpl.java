package com.meiyuan.catering.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.merchant.dao.CateringMerchantAuditMapper;
import com.meiyuan.catering.merchant.entity.CateringMerchantAuditEntity;
import com.meiyuan.catering.merchant.service.CateringMerchantAuditService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 商户审核表(CateringMerchantAudit)表服务实现类
 *
 * @author wxf
 * @since 2020-03-10 10:26:03
 */
@Service
public class CateringMerchantAuditServiceImpl extends ServiceImpl<CateringMerchantAuditMapper, CateringMerchantAuditEntity>
        implements CateringMerchantAuditService {
    @Resource
    private CateringMerchantAuditMapper cateringMerchantAuditMapper;

    
}