package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersDeliveryMapper;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryEntity;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryService;
import org.springframework.stereotype.Service;

/**
 * 订单配送表(CateringOrdersDelivery)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersDeliveryService")
public class CateringOrdersDeliveryServiceImpl extends ServiceImpl<CateringOrdersDeliveryMapper, CateringOrdersDeliveryEntity> implements CateringOrdersDeliveryService {

    @Override
    public OrderDelivery getByOrderId(Long orderId) {
        LambdaQueryWrapper<CateringOrdersDeliveryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersDeliveryEntity::getOrderId, orderId);
        return BaseUtil.objToObj(baseMapper.selectOne(wrapper), OrderDelivery.class);
    }
}
