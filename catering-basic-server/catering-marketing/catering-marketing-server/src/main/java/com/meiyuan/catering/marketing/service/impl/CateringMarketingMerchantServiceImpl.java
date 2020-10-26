package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.entity.IdEntity;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.marketing.dao.CateringMarketingMerchantMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingMerchantEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingMerchantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luohuan
 * @date 2020/3/24
 **/
@Service("cateringMarketingMerchantService")
public class CateringMarketingMerchantServiceImpl extends ServiceImpl<CateringMarketingMerchantMapper, CateringMarketingMerchantEntity>
        implements CateringMarketingMerchantService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByOfId(Long ofId) {
        LambdaQueryWrapper<CateringMarketingMerchantEntity> queryWrapper = new QueryWrapper<CateringMarketingMerchantEntity>().lambda()
                .eq(CateringMarketingMerchantEntity::getOfId, ofId);
        List<CateringMarketingMerchantEntity> merchantEntities = list(queryWrapper);
        List<Long> merchantIds = merchantEntities.stream()
                .map(IdEntity::getId)
                .collect(Collectors.toList());
        if (!merchantIds.isEmpty()) {
            removeByIds(merchantIds);
        }
    }

    @Override
    public CateringMarketingMerchantEntity getByOfId(Long ofId) {
        LambdaQueryWrapper<CateringMarketingMerchantEntity> queryWrapper = new QueryWrapper<CateringMarketingMerchantEntity>().lambda()
                .eq(CateringMarketingMerchantEntity::getOfId, ofId);
        return getOne(queryWrapper);
    }

    @Override
    public List<CateringMarketingMerchantEntity> listByOfIds(List<Long> ofIds) {
        LambdaQueryWrapper<CateringMarketingMerchantEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(CateringMarketingMerchantEntity::getOfId,ofIds)
                .eq(CateringMarketingMerchantEntity::getDel, DelEnum.NOT_DELETE.getFlag());
        return this.list(queryWrapper);
    }


}
