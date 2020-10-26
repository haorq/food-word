package com.meiyuan.catering.wx.utils.strategy;

import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.user.dto.cart.AddCartDTO;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.HandlerCartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 团购策略
 * 暂时只实现添加订单的检查
 *
 * @author zengzhangni
 * @date 2020/6/19 18:15
 * @since v1.1.0
 */
@Slf4j
@Component("bulkStrategy")
public class BulkStrategy extends BaseCartStrategy {


    @Override
    public void calculateCart(AddCartDTO dto) {
    }

    @Override
    public void cartGoodsCheck(CartGoodsCheckDTO checkDTO, Cart cart) {
        OrdersDetailGoodsDTO goods = new OrdersDetailGoodsDTO();
        goods.setGoodsId(cart.getGoodsId());
        //调用父类检查活动方法
        payOrderGoodsCheckByActivity(goods);
    }

    @Override
    public CartGoodsDTO handlerCartGoods(HandlerCartDTO handlerCartDTO, Cart cart) {
        return null;
    }


    @Override
    public void payOrderGoodsCheck(OrdersDetailDTO order, OrdersDetailGoodsDTO goods) {
        //调用父类检查活动方法
        payOrderGoodsCheckByActivity(goods);
    }

}
