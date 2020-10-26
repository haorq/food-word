package com.meiyuan.catering.order.service.calculate;


import com.meiyuan.catering.order.dto.calculate.OrderCalculateDTO;
import com.meiyuan.catering.order.enums.CalculateTypeEnum;
import com.meiyuan.catering.order.enums.DeliveryWayEnum;
import com.meiyuan.catering.user.dto.cart.CartSimpleDTO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单普通、秒杀、菜单商品验证、计算模板方法
 *
 * @Author XiJie-Xie
 * @create 2020/3/10 13:41
 */
@Component
public abstract class AbstractOrdersCalculate {

    /**
     * 功能描述: 获取商户店铺信息
     *
     * @param param 结算信息
     */
    public abstract void getShopInfo(OrderCalculateDTO param);

    /**
     * 功能描述: 获取配送信息
     *
     * @param param 结算信息
     */
    public abstract void getDeliveryInfo(OrderCalculateDTO param);

    /**
     * 描述:获取购物车商品信息
     *
     * @param param        结算信息
     * @param cartTypeEnum 购物车类型
     * @return java.util.List<com.meiyuan.catering.user.dto.cart.CartSimpleDTO>
     */
    public abstract List<CartSimpleDTO> listCartGoods(OrderCalculateDTO param, CalculateTypeEnum cartTypeEnum);

    /**
     * 功能描述: 验证优惠卷
     *
     * @param param 结算信息
     * @return: void
     */
    public abstract void couponsCheck(OrderCalculateDTO param);

    /**
     * 功能描述: 购物车商品验证
     *
     * @param cateringCart 购物车信息
     * @param param        结算信息
     */
    public abstract void cartGoodsCheck(List<CartSimpleDTO> cateringCart, OrderCalculateDTO param);

    /**
     * 功能描述: 团购商品校验
     *
     * @param param 结算信息
     * @return: void
     */
    public abstract void bulkGoodsCheck(OrderCalculateDTO param);

    /**
     * 功能描述: 优惠券针对哪些商品使用
     *
     * @param param
     * @return: void
     */
    public abstract void discountInfo(OrderCalculateDTO param);

    /**
     * 功能描述: 计算金额
     *
     * @param param 结算信息
     */
    public abstract void calculateAmount(OrderCalculateDTO param);

    /**
     * 功能描述: 获取赠品信息
     *
     * @param param
     * @return: void
     */
    public abstract void listShopGift(OrderCalculateDTO param);

    /**
     * 开启购物车验证、计算模板方法
     *
     * @param param        购物车结算请求信息
     * @param cartTypeEnum 请求购物车类型 1--菜单，2--商品/秒杀，3--拼单
     */
    public void startCartCheck(OrderCalculateDTO param, CalculateTypeEnum cartTypeEnum) {
        // 获取商家信息
        this.getShopInfo(param);
        // 获取购物车信息
        List<CartSimpleDTO> cateringCartEntities = this.listCartGoods(param, cartTypeEnum);

        // 如果优惠卷ID不为空。先验证优惠卷（v1.3.0新增门店优惠券）
        if (param.getCouponsId() != null || param.getCouponsWithShopId() != null) {
            // 验证优惠卷是否可使用
            this.couponsCheck(param);
        }
        // 购物车商品验证
        this.cartGoodsCheck(cateringCartEntities, param);
        // 处理用户配送信息
        this.getDeliveryInfo(param);
        // 优惠卷针对商品的处理
        this.discountInfo(param);
        // 计算金额
        this.calculateAmount(param);
        // 如果是自提
        if (DeliveryWayEnum.invite.getCode().equals(param.getDeliveryWay())) {
            // 获取自提赠品信息
            this.listShopGift(param);
        }
    }


    /**
     * 开启团购验证、计算模板方法
     *
     * @param param 团购结算请求信息
     */
    public void startBulkCheck(OrderCalculateDTO param) {
        // 获取商家信息
        this.getShopInfo(param);
        // 团购商品验证
        this.bulkGoodsCheck(param);
        // 处理用户配送信息
        this.getDeliveryInfo(param);
        // 计算金额
        this.calculateAmount(param);
        // 如果是自提
        if (DeliveryWayEnum.invite.getCode().equals(param.getDeliveryWay())) {
            // 获取自提赠品信息
            this.listShopGift(param);
        }
    }
}
