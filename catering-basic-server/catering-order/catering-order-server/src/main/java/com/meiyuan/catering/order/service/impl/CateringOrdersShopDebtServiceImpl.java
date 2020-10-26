package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersShopDebtMapper;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtEntity;
import com.meiyuan.catering.order.service.CateringOrdersShopDebtFlowService;
import com.meiyuan.catering.order.service.CateringOrdersShopDebtService;
import com.meiyuan.catering.order.service.CateringOrdersSplitBillSubsidyFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商户负债服务接口实现
 **/

@Slf4j
@Service("cateringOrdersShopDebtService")
public class CateringOrdersShopDebtServiceImpl
        extends ServiceImpl<CateringOrdersShopDebtMapper, CateringOrdersShopDebtEntity> implements CateringOrdersShopDebtService {

    @Autowired
    private CateringOrdersShopDebtFlowService ordersShopDebtFlowService;
    @Autowired
    private CateringOrdersSplitBillSubsidyFlowService ordersSplitBillSubsidyFlowService;

    @Override
    public void initCreate(List<Long> shopIdList) {
        // 先查询之前存在的负债信息
        List<CateringOrdersShopDebtEntity> existedList = list();
        // 过滤掉已经存在的门店ID
        if (BaseUtil.judgeList(existedList)) {
            List<Long> existedShopIdList = existedList.stream().map(CateringOrdersShopDebtEntity::getShopId).collect(Collectors.toList());
            shopIdList = shopIdList.stream().filter(item -> !existedShopIdList.contains(item)).collect(Collectors.toList());
        }
        // 初始化默认信息，并保存
        if (BaseUtil.judgeList(shopIdList)) {
            LocalDateTime now = LocalDateTime.now();
            List<CateringOrdersShopDebtEntity> collect = shopIdList.stream().map(item -> {
                CateringOrdersShopDebtEntity entity = new CateringOrdersShopDebtEntity();
                entity.setId(IdWorker.getId());
                entity.setShopId(item);
                entity.setAmount(BigDecimal.ZERO);
                entity.setCreateTime(now);
                return entity;
            }).collect(Collectors.toList());
            // 批量保存
            saveBatch(collect);
        }
    }

    @Override
    public void create(Long shopId) {
        // 查询之前是否创建了负债信息
        LambdaQueryWrapper<CateringOrdersShopDebtEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersShopDebtEntity::getShopId, shopId);
        CateringOrdersShopDebtEntity entity = baseMapper.selectOne(queryWrapper);
        // 存在就不再创建；不存在，则创建对应门店的初始化信息
        if (null == entity) {
            entity = new CateringOrdersShopDebtEntity();
            entity.setId(IdWorker.getId());
            entity.setShopId(shopId);
            entity.setAmount(BigDecimal.ZERO);
            entity.setCreateTime(LocalDateTime.now());
            // 保存
            save(entity);
        }
    }

    @Override
    public Boolean accumulate(Long shopId, BigDecimal debtAmount) {
        return baseMapper.accumulate(shopId, debtAmount);
    }

    @Override
    public Boolean subtractDebtAmount(Long shopId, BigDecimal debtAmount) {
        return baseMapper.subtractDebtAmount(shopId, debtAmount);
    }

    @Override
    public CateringOrdersShopDebtEntity queryByShopId(Long shopId) {
        LambdaQueryWrapper<CateringOrdersShopDebtEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersShopDebtEntity::getShopId, shopId);
        return baseMapper.selectOne(wrapper);
    }


}
