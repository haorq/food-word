package com.meiyuan.catering.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.meiyuan.catering.order.entity.CateringOrdersShopDebtFlowEntity;

import java.util.List;

/**
 * @author GongJunZheng
 * @date 2020/10/13 11:10
 * @description 商户负债明细服务接口
 **/

public interface CateringOrdersShopDebtFlowService extends IService<CateringOrdersShopDebtFlowEntity> {

    List<CateringOrdersShopDebtFlowEntity> listDebtFlow(Long orderId, Integer type);

    CateringOrdersShopDebtFlowEntity queryRollBackDebt(Long orderId);
}
