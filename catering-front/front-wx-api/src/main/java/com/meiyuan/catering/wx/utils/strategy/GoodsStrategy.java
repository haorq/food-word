package com.meiyuan.catering.wx.utils.strategy;

import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.goods.EsGoodsDTO;
import com.meiyuan.catering.merchant.goods.dto.shop.ShopSkuDTO;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsSkuClient;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.user.dto.cart.AddCartDTO;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import com.meiyuan.catering.user.enums.CartGoodsTypeEnum;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.utils.WxCommonUtil;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.HandlerCartDTO;
import com.meiyuan.catering.wx.utils.strategy.helper.GoodsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/6/19 18:15
 * @since v1.1.0
 */
@Slf4j
@Component("goodsStrategy")
public class GoodsStrategy extends BaseCartStrategy {

    @Autowired
    private GoodsHelper goodsHelper;
    @Resource
    private ShopGoodsSkuClient shopGoodsSkuClient;


    @Override
    public void calculateCart(AddCartDTO dto) {
        Integer userType = dto.getUserType();

        //获取商品信息
        EsGoodsDTO esGood = goodsHelper.getBySkuCode(dto.getShopId(), dto.getGoodsId(), dto.getSkuCode());

        BigDecimal totalPrice = goodsHelper.getGoodsPrice(userType, dto.getNumber(), dto.getHaveNumber(), esGood);
        dto.setTotalPrice(totalPrice);
        dto.setMarketPrice(esGood.getMarketPrice());
        dto.setEnterprisePrice(esGood.getEnterprisePrice());
        dto.setGoodsSpecType(esGood.getGoodsSpecType());
        dto.setPrice(BigDecimal.ZERO);
        dto.setCategoryId(Long.valueOf(esGood.getCategoryId()));
        dto.setGoodsStatus(esGood.getGoodsStatus());
        dto.setGoodsName(goodsHelper.goodsName(esGood));
        dto.setPackPrice(esGood.getPackPrice());
        if (CartUtil.isCompanyUser(userType)) {
            dto.setSalesPrice(BaseUtil.PRICE);
            dto.setSeckillEventId(BaseUtil.NULL_FLAG);
            dto.setGoodsType(CartGoodsTypeEnum.ORDINARY.getStatus());
            dto.setDiscountLimit(BaseUtil.NULL_FLAG.intValue());
        } else {
            dto.setDiscountLimit(esGood.getDiscountLimit());
            dto.setSalesPrice(esGood.getSalesPrice());
        }

    }

    @Override
    public void cartGoodsCheck(CartGoodsCheckDTO checkDTO, Cart cart) {
        //去结算验证购物车商品
        goodsHelper.submitCartVerify(cart, checkDTO);
    }


    @Override
    public CartGoodsDTO handlerCartGoods(HandlerCartDTO handlerCartDTO, Cart cart) {

        EsGoodsDTO esGoodsDTO = goodsHelper.getBySkuCode(cart.getShopId().toString(), cart.getGoodsId().toString(), cart.getSkuCode());

        if (esGoodsDTO != null && goodsHelper.goodsIsUpper(esGoodsDTO) && !goodsHelper.salesChannelIsIncludeWx(esGoodsDTO)) {
            CartGoodsDTO wxCartGoodsDTO = BaseUtil.objToObj(esGoodsDTO, CartGoodsDTO.class);
            wxCartGoodsDTO.setSkuCode(cart.getSkuCode());
            Integer lowestBuy = WxCommonUtil.getLowestBuy(CartUtil.isCompanyUser(cart.getUserType()), esGoodsDTO.getLowestBuy(), esGoodsDTO.getMinQuantity());
            wxCartGoodsDTO.setLowestBuy(lowestBuy);
            BigDecimal totalPrice;
            if (Objects.equals(handlerCartDTO.getUserId(), cart.getUserId())) {
                totalPrice = goodsHelper.getGoodsPrice(cart.getUserType(), cart.getNumber(), esGoodsDTO);
            } else {
                totalPrice = cart.getTotalPrice();
            }
            wxCartGoodsDTO.setTotalPrice(totalPrice);

            return wxCartGoodsDTO;
        }

        return null;
    }

    @Override
    public void payOrderGoodsCheck(OrdersDetailDTO order, OrdersDetailGoodsDTO goods) {
        EsGoodsDTO esGood = goodsHelper.getBySkuCode(order.getStoreId().toString(), goods.getGoodsId().toString(), goods.getGoodsSkuCode());
        if (esGood == null) {

            Result<ShopSkuDTO> result = shopGoodsSkuClient.queryBySkuAndShopId(goods.getGoodsSkuCode(), goods.getShopId());
            if (result.failure()) {
                throw new CustomException(result.getCode(), result.getMsg());
            }
            ShopSkuDTO dto = result.getData();
            if (dto == null) {
                throw new CustomException(result.getMsg());
            }
            throw new CustomException(ErrorCode.GROUPON_DOWN, dto.getGoodsName() + "商品已下架，请重新下单");

        }
        String goodsName = goodsHelper.goodsName(esGood);

        Boolean lower = goodsHelper.goodsIsLower(esGood);
        if (lower) {
            log.warn("{}:已下架", goodsName);
            throw new CustomException(ErrorCode.GROUPON_DOWN, goodsName + "已下架，请重新下单!");
        }

        Boolean includeWx = goodsHelper.salesChannelIsIncludeWx(esGood);
        if (includeWx) {
            log.warn("{}:售卖渠道不支持小程序", goodsName);
            throw new CustomException(goodsName + "不存在，请重新下单！");
        }


        if (goodsHelper.goodsIsPresell(esGood)) {
            // 预售商品
            throw new CustomException(goodsName + "不再预售时间范围内，请重新下单！");
        }


        BigDecimal totalPrice = goodsHelper.getGoodsPrice(order.getMemberType(), goods.getQuantity(), esGood);
        if (BaseUtil.priceNoEquals(totalPrice, goods.getDiscountLaterFee())) {
            log.warn("{}:价格发生变化", goodsName);
            throw new CustomException(ErrorCode.ORDER_GOODS_PRICE_CHANGE, goodsName + "已变更价格，请重新下单！");
        }

        //商品包装费价格是否发生变化-- [返回到购物车列表，重新计算价格，需重新下单]
        BigDecimal orderGoodsPackPrice = goods.getPackPrice();
        BigDecimal esPackPrice = esGood.getPackPrice();
        if (orderGoodsPackPrice != null && esPackPrice != null) {
            //餐盒费默认为-1,这里特殊处理为0
            esPackPrice = BaseUtil.isNullOrNegativeOne(esPackPrice) ? BigDecimal.ZERO : esPackPrice;
            Boolean packChange = BaseUtil.priceNoEquals(orderGoodsPackPrice, esPackPrice);
            if (packChange) {
                log.warn("{}:餐盒费价格发生变化", goodsName);
                throw new CustomException(goodsName + "餐盒费价格发生变化，请重新下单！");
            }
        }


    }
}
