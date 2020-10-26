package com.meiyuan.catering.order.feign;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.entity.CateringOrdersDiscountsEntity;
import com.meiyuan.catering.order.enums.OrderDiscountTypeEnum;
import com.meiyuan.catering.order.service.CateringOrdersDiscountsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author zengzhangni
 * @date 2020/5/20 9:39
 * @since v1.1.0
 */
@Service
public class OrderDiscountsClient {
    @Resource
    private CateringOrdersDiscountsService service;

    /**
     * 描述:查询订单优惠卷id
     *
     * @param orderId
     * @return java.lang.Long
     * @author zengzhangni
     * @date 2020/5/20 10:53
     * @since v1.1.0
     */
    public Result<Long> getDiscountId(Long orderId) {
        return Result.succ(service.getDiscountId(orderId));
    }


    /**
     * 根据订单ID查询订单使用的优惠券 v1.3.0 lh 优惠券支持2张同时使用
     * @param orderId
     * @return
     */
    public Result<List<CateringOrdersDiscountsEntity>> list(Long orderId) {
        LambdaQueryWrapper<CateringOrdersDiscountsEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CateringOrdersDiscountsEntity::getOrderId, orderId);
        queryWrapper.eq(CateringOrdersDiscountsEntity::getDiscountType, OrderDiscountTypeEnum.TICKET.getCode());
        return Result.succ(service.list(queryWrapper));
    }

}
