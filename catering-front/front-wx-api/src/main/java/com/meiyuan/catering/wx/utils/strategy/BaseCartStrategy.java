package com.meiyuan.catering.wx.utils.strategy;

import com.meiyuan.catering.core.dto.base.MerchantInfoDTO;
import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.enums.base.StatusEnum;
import com.meiyuan.catering.core.enums.base.merchant.BusinessStatusEnum;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.marketing.enums.MarketingUpDownStatusEnum;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.user.dto.cart.AddCartDTO;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.HandlerCartDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/6/19 18:14
 * @since v1.1.0
 */
@Slf4j
public abstract class BaseCartStrategy {

    @Autowired
    private MerchantUtils merchantUtils;
    //    @Resource
//    private MarketingGoodsClient marketingGoodsClient;
    @Resource
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private EsGoodsClient esGoodsClient;


    /**
     * 描述:购物车验证/计算
     *
     * @param dto
     * @return void
     * @author zengzhangni
     * @date 2020/6/22 9:05
     * @since v1.1.1
     */
    public abstract void calculateCart(AddCartDTO dto);


    public void calculateCartByDefault(AddCartDTO dto) {

        shopAndMerchantStatusVerify(dto.getShopId(), false);
        //购物车验证/计算
        calculateCart(dto);
    }


    /**
     * 描述:提交购物车 购物车商品检查
     *
     * @param checkDTO
     * @param cart
     * @return java.lang.Boolean 错误信息
     * @author zengzhangni
     * @date 2020/6/22 16:31
     * @since v1.1.1
     */
    public abstract void cartGoodsCheck(CartGoodsCheckDTO checkDTO, Cart cart);


    /**
     * 描述:提交购物车 购物车商品检查
     *
     * @param checkDTO
     * @param cart
     * @return java.lang.String
     * @author zengzhangni
     * @date 2020/6/24 9:06
     * @since v1.1.1
     */
    public void cartGoodsCheckByDefault(CartGoodsCheckDTO checkDTO, Cart cart) {

        shopAndMerchantStatusVerify(cart.getShopId(), true);

        //执行子类检查
        cartGoodsCheck(checkDTO, cart);
    }


    /**
     * 描述:处理购物车商品信息
     *
     * @param handlerCartDTO
     * @param cart           购物车信息
     * @return com.meiyuan.catering.es.dto.goods.EsGoodsDTO
     * @author zengzhangni
     * @date 2020/6/23 16:56
     * @since v1.1.1
     */
    public abstract CartGoodsDTO handlerCartGoods(HandlerCartDTO handlerCartDTO, Cart cart);


    /**
     * 描述:处理购物车商品信息
     *
     * @param handlerCartDTO
     * @param cart           购物车信息
     * @return void 暂不需要返回值
     * @author zengzhangni
     * @date 2020/6/23 16:56
     * @since v1.1.1
     */
    public void baseHandlerCartGoods(HandlerCartDTO handlerCartDTO, Cart cart) {

        //调用子类实现 获取出来后的商品信息
        CartGoodsDTO wxCartGoodsDTO = handlerCartGoods(handlerCartDTO, cart);

        if (wxCartGoodsDTO != null) {
            //组装购物车信息
            wxCartGoodsDTO.setId(cart.getId());
            wxCartGoodsDTO.setMerchantId(cart.getMerchantId());
            wxCartGoodsDTO.setGoodsId(cart.getGoodsId());
            wxCartGoodsDTO.setNumber(cart.getNumber());
            wxCartGoodsDTO.setType(cart.getType());
            wxCartGoodsDTO.setGoodsType(cart.getGoodsType());
            wxCartGoodsDTO.setCategoryId(cart.getCategoryId());
            wxCartGoodsDTO.setPrice(cart.getSalesPrice());
            wxCartGoodsDTO.setUserId(cart.getUserId());
            wxCartGoodsDTO.setSeckillEventId(cart.getSeckillEventId());
            wxCartGoodsDTO.setPackPrice(cart.getPackPrice());

            Integer number = cart.getNumber();

            handlerCartDTO.addResult(wxCartGoodsDTO);
            handlerCartDTO.addTotalAmt(wxCartGoodsDTO.getTotalPrice());
            handlerCartDTO.addTotalOldAmt(BaseUtil.multiply(wxCartGoodsDTO.getMarketPrice(), number));
            handlerCartDTO.addTotalPackAmt(wxCartGoodsDTO.getPackPrice(), number);

            handlerCartDTO.addGoodsNum(wxCartGoodsDTO.getNumber());
        } else {
            handlerCartDTO.addDeleteCartIds(cart.getId());
        }
    }


    /**
     * 描述: 去支付订单商品验证
     *
     * @param order
     * @param goods
     * @return void
     * @author zengzhangni
     * @date 2020/7/8 14:26
     * @since v1.2.0
     */
    public abstract void payOrderGoodsCheck(OrdersDetailDTO order, OrdersDetailGoodsDTO goods);

    public void payOrderGoodsCheckByDefault(OrdersDetailDTO order, OrdersDetailGoodsDTO goods) {

        shopAndMerchantStatusVerify(order.getStoreId(), false);
        //子类检查
        payOrderGoodsCheck(order, goods);
    }


    /**
     * 描述:秒杀、团购 验证是一样 提取到父类
     *
     * @param goods
     * @return void
     * @author zengzhangni
     * @date 2020/7/8 14:39
     * @since v1.2.0
     */
    public void payOrderGoodsCheckByActivity(OrdersDetailGoodsDTO goods) {
        // 获取秒杀、团购信息
        EsMarketingDTO marketingDTO = ClientUtil.getDate(esGoodsClient.getMarketingGoodsById(goods.getGoodsId()));
        if (marketingDTO == null) {
            // 商品被删除的场景
            Result<String> result = merchantGoodsClient.getGoodsNameFromDbByMgoodsId(goods.getGoodsId());
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, result.getData() + "商品已下架，请重新下单");
        }

        if (!LocalDateTime.now().isBefore(marketingDTO.getEndTime())
                //活动下架
                || Objects.equals(MarketingUpDownStatusEnum.DOWN.getStatus(), marketingDTO.getUpDownState())
        ) {
            log.error("订单商品已下架，不能完成支付 simpleInfo =【{}】", marketingDTO);
            throw new CustomException(ErrorCode.ACTIVITY_FREEZE_ERROR_MSG);
        }

        //菜单移除商品 / 商品下架 /不支持微信售卖渠道
        if (marketingDTO.getDel() ||
                Objects.equals(MarketingUpDownStatusEnum.DOWN.getStatus(), marketingDTO.getGoodsUpDownState())
                || CartUtil.nonsupportWx(marketingDTO)
        ) {

            log.error("订单商品已下架，不能完成支付 marketingDTO =【{}】", marketingDTO);
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, marketingDTO.getGoodsName() + "商品已下架，请重新下单");
        }


        BigDecimal packPrice = BaseUtil.isNullOrNegativeOne(marketingDTO.getPackPrice()) ? BigDecimal.ZERO : marketingDTO.getPackPrice();
        if (!BaseUtil.priceEquals(goods.getPackPrice(), packPrice)) {
            log.error("订单商品餐盒费发生变更，不能完成支付 marketingDTO =【{}】", marketingDTO);
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, marketingDTO.getGoodsName() + ",餐盒费发生变更");
        }


    }


    /**
     * 描述:门店 和 商户状态验证
     *
     * @param shopId
     * @param judgeBusinessHours 是否判断营业时间
     * @return void
     * @author zengzhangni
     * @date 2020/7/8 14:31
     * @since v1.2.0
     */
    private void shopAndMerchantStatusVerify(Long shopId, Boolean judgeBusinessHours) {
        ShopInfoDTO shop = merchantUtils.getShopIsNullThrowEx(shopId);

        MerchantInfoDTO merchant = merchantUtils.getMerchantIsNullThrowEx(shop.getMerchantId());

        //品牌是否禁用
        if (!Objects.equals(merchant.getMerchantStatus(), StatusEnum.ENABLE.getStatus())) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }

        //1.门店是否禁用(1.品牌/2.门店是否禁用) -- [直接报错]
        if (!Objects.equals(shop.getShopStatus(), StatusEnum.ENABLE.getStatus())) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.MERCHANT_STATUS_MSG);
        }

        if (judgeBusinessHours) {
            //2.门店是否营业中(根据营业时间判断) -- [直接报错]
            if (!Objects.equals(shop.getBusinessStatus(), BusinessStatusEnum.OPEN.getStatus())) {
                throw new CustomException(ErrorCode.SHOP_BUSINESS_STATUS_ERROR, ErrorCode.SHOP_BUSINESS_STATUS_MSG);
            }
        }
    }

}
