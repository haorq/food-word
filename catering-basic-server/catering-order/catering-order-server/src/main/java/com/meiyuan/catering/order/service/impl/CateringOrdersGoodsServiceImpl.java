package com.meiyuan.catering.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.meiyuan.catering.core.dto.order.goods.OrderGoods;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.order.dao.CateringOrdersGoodsMapper;
import com.meiyuan.catering.order.entity.CateringOrdersGoodsEntity;
import com.meiyuan.catering.order.service.CateringOrdersGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单商品表(CateringOrdersGoods)表服务实现类
 *
 * @author xie-xi-jie
 * @since 2020-03-16 11:16:41
 */
@Service("cateringOrdersGoodsService")
public class CateringOrdersGoodsServiceImpl extends ServiceImpl<CateringOrdersGoodsMapper, CateringOrdersGoodsEntity> implements CateringOrdersGoodsService {

    @Override
    public List<OrderGoods> getByOrderId(Long orderId) {
        LambdaQueryWrapper<CateringOrdersGoodsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringOrdersGoodsEntity::getOrderId, orderId);
        return BaseUtil.objToObj(baseMapper.selectList(wrapper), OrderGoods.class);
    }
}
