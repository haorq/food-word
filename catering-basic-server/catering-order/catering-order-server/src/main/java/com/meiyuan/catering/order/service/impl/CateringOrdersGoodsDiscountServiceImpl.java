package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.order.dao.CateringOrdersGoodsDiscountMapper;
import com.meiyuan.catering.order.entity.CateringOrdersGoodsDiscountEntity;
import com.meiyuan.catering.order.service.CateringOrdersGoodsDiscountService;
import org.springframework.stereotype.Service;

/**
 * 订单商品优惠详情表(CateringOrdersGoodsDiscount)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersGoodsDiscountService")
public class CateringOrdersGoodsDiscountServiceImpl extends ServiceImpl<CateringOrdersGoodsDiscountMapper, CateringOrdersGoodsDiscountEntity> implements CateringOrdersGoodsDiscountService {

}
