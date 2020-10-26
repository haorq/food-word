package com.meiyuan.catering.order.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtFlowEntity;
import com.meiyuan.catering.order.enums.AmountTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersShopDebtFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商户负债明细服务聚合层
 **/

@Service
public class OrdersShopDebtFlowClient {

    @Autowired
    private CateringOrdersShopDebtFlowService ordersShopDebtFlowService;

    public void save(CateringOrdersShopDebtFlowEntity debtFlowEntity) {
        ordersShopDebtFlowService.save(debtFlowEntity);
    }

    public Result<List<CateringOrdersShopDebtFlowEntity>> listDebtFlow(Long orderId, Integer type) {
        return Result.succ(ordersShopDebtFlowService.listDebtFlow(orderId, type));
    }

    public CateringOrdersShopDebtFlowEntity queryRollBackDebt(Long orderId) {
        return ordersShopDebtFlowService.queryRollBackDebt(orderId);
    }

    public Boolean isFirstDelivery(Long orderId) {
        LambdaQueryWrapper<CateringOrdersShopDebtFlowEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersShopDebtFlowEntity::getOrderId, orderId)
                .eq(CateringOrdersShopDebtFlowEntity::getAmountType, AmountTypeEnum.COPE_WITH.getType());
        int count = ordersShopDebtFlowService.count(wrapper);

        return count == 0;
    }
}
