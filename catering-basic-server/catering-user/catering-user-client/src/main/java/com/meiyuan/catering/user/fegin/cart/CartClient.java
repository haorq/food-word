package com.meiyuan.catering.user.fegin.cart;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.user.dto.cart.*;
import com.meiyuan.catering.user.entity.CateringCartEntity;
import com.meiyuan.catering.user.enums.CartTypeEnum;
import com.meiyuan.catering.user.service.CateringCartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author zengzhangni
 * @date 2020/6/18 11:59
 * @since v1.1.0
 */
@Service
public class CartClient {

    @Autowired
    private CateringCartService cartService;

    /**
     * @param dto 添加购物车参数
     * @return Result
     * @description 添加购物车
     * @author yaozou
     * @date 2020/5/19 16:17
     * @since v1.1.0
     */
    public Result add(AddCartDTO dto) {
        return Result.succ(cartService.add(dto));
    }

    /**
     * @param cartDTO
     * @return {@link Result}
     * @description 清空购物车
     * @author yaozou
     * @date 2020/5/19 16:17
     * @since v1.1.0
     */
    public Result clear(ClearCartDTO cartDTO) {
        return Result.succ(cartService.clear(cartDTO));
    }

    /**
     * @param cartDTO
     * @return {@link Result}
     * @description 生成订单，清空购物车
     * @author yaozou
     * @date 2020/5/19 16:17
     * @since v1.1.0
     */
    public Result clearForCreateOrder(ClearCartDTO cartDTO) {
        return Result.succ(cartService.clearForCreateOrder(cartDTO));
    }

    /**
     * @param
     * @return
     * @description 购物车基本数据
     * @author yaozou
     * @date 2020/5/20 9:39
     * @since v1.0.0
     */
    public Result<List<CartSimpleDTO>> listSimple(CartGoodsQueryDTO queryDTO) {
        return Result.succ(ConvertUtils.sourceToTarget(listEntity(queryDTO), CartSimpleDTO.class));
    }


    /**
     * 创建拼单
     * 描述: 购物车类型改变为拼单
     *
     * @param merchantId
     * @param shopId
     * @param shareBillNo
     * @param shareUserId
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/18 9:54
     * @since v1.1.0
     */
    public Boolean cartTypeToShareBill(Long merchantId, Long shopId, String shareBillNo, Long shareUserId) {
        return cartService.cartTypeToShareBill(merchantId, shopId, shareBillNo, shareUserId);
    }

    /**
     * 取消拼单
     * 描述:购物车类型改变为普通
     *
     * @param shareBillNo
     * @return java.lang.Boolean
     * @author zengzhangni
     * @date 2020/6/18 9:55
     * @since v1.1.0
     */
    public Boolean cartTypeToGoods(String shareBillNo) {
        return cartService.cartTypeToGoods(shareBillNo);
    }

    public List<Cart> countSelectedNumber(CartCountSelectedNumDTO dto) {
        return cartService.countSelectedNumber(dto);
    }


    public List<Cart> listCheckCart(CartMerchantDTO merchantDTO) {
        Long merchantId = merchantDTO.getMerchantId();
        Long shopId = merchantDTO.getShopId();
        String shareBillNo = merchantDTO.getShareBillNo();
        Long userId = merchantDTO.getUserId();
        //是否是拼单人 只检查自己的购物车
        Boolean sharePeople = merchantDTO.getSharePeople();

        if (StringUtils.isNotBlank(shareBillNo)) {
            //拼单结算查询购物车
            return listCartGoodsByShareBill(merchantId, shopId, shareBillNo, sharePeople ? userId : null);
        } else {
            //普通购物车
            CartGoodsQueryDTO queryDTO = new CartGoodsQueryDTO();
            queryDTO.setMerchantId(merchantId);
            queryDTO.setShopId(shopId);
            queryDTO.setUserId(userId);
            queryDTO.setUserType(merchantDTO.getUserType());
            queryDTO.setType(CartTypeEnum.ORDINARY.getStatus());
            return listEntity(queryDTO);
        }
    }

    public List<Cart> listEntity(CartGoodsQueryDTO queryDTO) {
        return cartService.listCartGoods(queryDTO);
    }

    public List<Cart> listCartGoodsByShareBill(Long merchantId, Long shopId, String shareBillNo, Long userId) {
        return cartService.listCartGoodsByShareBill(merchantId, shopId, shareBillNo, userId);
    }

    public void removeByIds(Collection<Long> deleteCartIds) {
        cartService.removeByIds(deleteCartIds);
    }

    public void existDelGoods(String shareBillNo, Long userId) {
        cartService.existDelGoods(shareBillNo, userId);
    }

    public void cancelDelGoods(String shareBillNo, Long userId) {
        cartService.cancelDelGoods(shareBillNo, userId);
    }

    public Integer getCartSeckillNum(CartSeckillNumDTO numDTO) {
        return cartService.getCartSeckillNum(numDTO);
    }

    public void updateCarts(Set<Cart> recalculateCart) {
        cartService.updateCarts(recalculateCart);
    }


    public List<Cart> queryByShareBillNo(String shareBillNo) {
        LambdaQueryWrapper<CateringCartEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CateringCartEntity::getShareBillNo, shareBillNo)
                .orderByDesc(CateringCartEntity::getCreateTime);
        List<CateringCartEntity> list = cartService.list(wrapper);
        return BaseUtil.objToObj(list, Cart.class);
    }
}
