package com.meiyuan.catering.wx.utils.strategy.dto;

import com.meiyuan.catering.core.dto.cart.Cart;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zengzhangni
 * @date 2020/7/3 14:22
 * @since v1.1.0
 */
@Data
public class CartGoodsCheckDTO {

    /**
     * 描述: 删除购物车商品,提示用户
     * -----cartId
     * <p>
     * ---1.商品是否存在
     * ---2.商品是否下架
     * ---3.商品售卖渠道是否发生改变
     * ---4.商品规格是否发生变化
     * ---5.剩余库存为0,提示xx商品已抢光,清空购物车中的该商品
     *
     * @date 2020/7/3 14:29
     * @since v1.2.0
     */
    Set<Long> delIds;

    /**
     * 描述:删除购物车商品,提示信息
     *
     * @date 2020/7/3 14:45
     * @since v1.2.0
     */
    Set<String> delErrMsg;


    /**
     * 描述:返回到购物车列表，刷新数据，需手动调整购买数量后重新下单
     * <p>
     * ---1.起售数量是否发生变化
     * ---2.商品是否超过预售时间
     * ---3.商品是否超过下单截止时间
     * ---4.库存不足,提示商品剩余库存数量
     *
     * @date 2020/7/3 14:32
     * @since v1.2.0
     */
    Set<String> promptMessage;


    /**
     * 描述:返回到购物车列表，重新计算价格
     * --重新计算过后的购物车
     * <p>
     * ---1.每单限x份优惠发生变化
     * ---2. 商品价格发生变化
     *
     * @date 2020/7/3 14:33
     * @since v1.2.0
     */
    Set<Cart> recalculateCart;

    /**
     * 描述:重新计算价格
     * --提示信息
     *
     * @date 2020/7/3 14:46
     * @since v1.2.0
     */
    Set<String> recalculateCartErrMsg;

    /**
     * 描述:(拼单)返回到购物车列表，重新计算价格
     * --重新计算过后的购物车
     * <p>
     * ---1.每单限x份优惠发生变化
     * ---2. 商品价格发生变化
     *
     * @date 2020/7/7 9:39
     * @since v1.2.0
     */
    Set<Long> goodsIds;

    Map<String, Integer> skuInventoryMap;

    public void addDelIds(Long delId) {
        this.delIds.add(delId);
    }


    public void addDelErrMsg(String delErrMsg) {
        this.delErrMsg.add(delErrMsg);
    }

    public void addPromptMessage(String promptMessage) {
        this.promptMessage.add(promptMessage);
    }

    public void addRecalculateCart(Cart recalculateCart) {
        this.recalculateCart.add(recalculateCart);
    }

    public void addRecalculateCartErrMsg(String recalculateCartErrMsg) {
        this.recalculateCartErrMsg.add(recalculateCartErrMsg);
    }

    public void addGoodsIds(Long goodsId) {
        this.goodsIds.add(goodsId);
    }



    public Integer getSkuInventory(String skuCode) {
        return skuInventoryMap.get(skuCode);
    }

    public CartGoodsCheckDTO() {
        this.delIds = new LinkedHashSet<>();
        this.delErrMsg = new LinkedHashSet<>();
        this.promptMessage = new LinkedHashSet<>();
        this.recalculateCart = new LinkedHashSet<>();
        this.recalculateCartErrMsg = new LinkedHashSet<>();
        this.goodsIds = new LinkedHashSet<>();
        this.skuInventoryMap = new HashMap<>();
    }
}
