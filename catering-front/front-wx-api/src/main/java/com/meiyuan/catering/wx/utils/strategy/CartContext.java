package com.meiyuan.catering.wx.utils.strategy;

import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.util.SpringContextUtils;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.user.dto.cart.AddCartDTO;
import com.meiyuan.catering.user.enums.CartGoodsTypeEnum;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.HandlerCartDTO;

/**
 * @author zengzhangni
 * @date 2020/7/2 13:45
 * @since v1.1.0
 */
public class CartContext {

    private BaseCartStrategy cartStrategy;

    public CartContext(Integer goodsType) {
        CartGoodsTypeEnum goodsTypeEnum = CartGoodsTypeEnum.parse(goodsType);
        this.cartStrategy = SpringContextUtils.getBean(goodsTypeEnum.getImpl(), BaseCartStrategy.class);
    }

    /**
     * 描述:购物车验证/计算
     *
     * @param dto
     * @return void
     * @author zengzhangni
     * @date 2020/7/2 13:51
     * @since v1.2.0
     */
    public void calculateCart(AddCartDTO dto) {
        cartStrategy.calculateCartByDefault(dto);
    }

    /**
     * 描述:提交购物车 购物车商品检查
     *
     * @param checkDTO
     * @param cart
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/7/2 13:51
     * @since v1.2.0
     */
    public void cartGoodsCheckByDefault(CartGoodsCheckDTO checkDTO, Cart cart) {
        cartStrategy.cartGoodsCheckByDefault(checkDTO, cart);
    }

    /**
     * 描述:处理购物车商品信息
     *
     * @param handlerCartDTO
     * @param cart
     * @return void
     * @author zengzhangni
     * @date 2020/7/2 13:51
     * @since v1.2.0
     */
    public void handlerCartGoods(HandlerCartDTO handlerCartDTO, Cart cart) {
        cartStrategy.baseHandlerCartGoods(handlerCartDTO, cart);
    }


    /**
     * 描述:已生成订单,待付款订单点击[去支付]时判断订单商品
     *
     * @param order
     * @param goods
     * @return void
     * @author zengzhangni
     * @date 2020/7/8 14:36
     * @since v1.2.0
     */
    public void payOrderGoodsCheck(OrdersDetailDTO order, OrdersDetailGoodsDTO goods) {
        cartStrategy.payOrderGoodsCheckByDefault(order, goods);
    }
}
