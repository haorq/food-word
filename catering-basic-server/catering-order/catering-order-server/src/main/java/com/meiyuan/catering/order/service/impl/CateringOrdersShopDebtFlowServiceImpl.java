package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.order.dao.CateringOrdersShopDebtFlowMapper;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtFlowEntity;
import com.meiyuan.catering.order.service.CateringOrdersShopDebtFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商户负债明细服务接口实现
 **/

@Slf4j
@Service("cateringOrdersShopDebtFlowService")
public class CateringOrdersShopDebtFlowServiceImpl
        extends ServiceImpl<CateringOrdersShopDebtFlowMapper, CateringOrdersShopDebtFlowEntity> implements CateringOrdersShopDebtFlowService {

    @Override
    public List<CateringOrdersShopDebtFlowEntity> listDebtFlow(Long orderId, Integer type) {
        LambdaQueryWrapper<CateringOrdersShopDebtFlowEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(CateringOrdersShopDebtFlowEntity::getOrderId, orderId)
                .eq(CateringOrdersShopDebtFlowEntity::getAmountType, type);
        return list(queryWrapper);
    }

    @Override
    public CateringOrdersShopDebtFlowEntity queryRollBackDebt(Long orderId) {
        return baseMapper.queryRollBackDebt(orderId);
    }
}
