package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.order.dao.CateringOrdersDiscountsMapper;
import com.meiyuan.catering.order.entity.CateringOrdersDiscountsEntity;
import com.meiyuan.catering.order.enums.OrderDiscountTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersDiscountsService;
import org.springframework.stereotype.Service;

/**
 * 订单优惠信息表(CateringOrdersDiscounts)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersDiscountsService")
public class CateringOrdersDiscountsServiceImpl extends ServiceImpl<CateringOrdersDiscountsMapper, CateringOrdersDiscountsEntity> implements CateringOrdersDiscountsService {


    @Override
    public Long getDiscountId(Long orderId) {
        LambdaQueryWrapper<CateringOrdersDiscountsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringOrdersDiscountsEntity::getOrderId, orderId);
        queryWrapper.eq(CateringOrdersDiscountsEntity::getDiscountType, OrderDiscountTypeEnum.TICKET.getCode());
        CateringOrdersDiscountsEntity one = this.baseMapper.selectOne(queryWrapper);
        if (one != null) {
            return one.getDiscountId();
        }
        return null;
    }
}
