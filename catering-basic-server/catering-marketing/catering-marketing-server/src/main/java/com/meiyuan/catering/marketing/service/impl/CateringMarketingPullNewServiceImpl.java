package com.meiyuan.catering.marketing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.enums.base.DelEnum;
import com.meiyuan.catering.marketing.dao.CateringMarketingPullNewMapper;
import com.meiyuan.catering.marketing.entity.CateringMarketingPullNewEntity;
import com.meiyuan.catering.marketing.service.CateringMarketingPullNewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author GongJunZheng
 * @date 2020/08/04 16:08
 * @description 秒杀拉新用户明细服务层实现
 **/

@Slf4j
@Service("cateringMarketingPullNewService")
public class CateringMarketingPullNewServiceImpl extends ServiceImpl<CateringMarketingPullNewMapper,
        CateringMarketingPullNewEntity> implements CateringMarketingPullNewService {

    @Override
    public Integer marketingPullCount(Long marketingId) {
        LambdaQueryWrapper<CateringMarketingPullNewEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringMarketingPullNewEntity :: getOfId, marketingId);
        return count(queryWrapper);
    }

    @Override
    public void insertPullNew(Long ofId, Integer ofType, Long userId, Long orderId) {
        CateringMarketingPullNewEntity entity = new CateringMarketingPullNewEntity();
        entity.setId(IdWorker.getId());
        entity.setOfId(ofId);
        entity.setOfType(ofType);
        entity.setUserId(userId);
        entity.setOrderId(orderId);
        entity.setCreateTime(LocalDateTime.now());
        entity.setDel(DelEnum.NOT_DELETE.getFlag());
        save(entity);
    }

    @Override
    public void delPullNew(Long orderId) {
        if(null == orderId) {
            return;
        }
        LambdaUpdateWrapper<CateringMarketingPullNewEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(CateringMarketingPullNewEntity :: getOrderId, orderId);
        remove(updateWrapper);
    }
}
