package com.meiyuan.catering.wx.utils.strategy;

import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ClientUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.es.dto.marketing.EsMarketingDTO;
import com.meiyuan.catering.es.enums.goods.GoodsStatusEnum;
import com.meiyuan.catering.es.enums.marketing.MarketingStatusEnum;
import com.meiyuan.catering.es.fegin.EsGoodsClient;
import com.meiyuan.catering.marketing.feign.MarketingRepertoryClient;
import com.meiyuan.catering.marketing.feign.MarketingSeckillEventRelationClient;
import com.meiyuan.catering.marketing.vo.seckillevent.MarketingSeckillEventVO;
import com.meiyuan.catering.merchant.goods.fegin.MerchantGoodsClient;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.user.dto.cart.AddCartDTO;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import com.meiyuan.catering.user.dto.cart.CartSeckillNumDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillBaseDTO;
import com.meiyuan.catering.user.enums.CartTypeEnum;
import com.meiyuan.catering.user.fegin.cart.CartClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillClient;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.HandlerCartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author zengzhangni
 * @date 2020/6/19 18:15
 * @since v1.1.0
 */
@Component("seckillStrategy")
public class SeckillStrategy extends BaseCartStrategy {

    @Autowired
    private EsGoodsClient esGoodsClient;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private CartShareBillClient shareBillClient;
    //    @Autowired
//    private MarketingSeckillClient seckillClient;
    @Autowired
    private MarketingSeckillEventRelationClient seckillEventRelationClient;
    @Autowired
    private MerchantGoodsClient merchantGoodsClient;
    @Autowired
    private MarketingRepertoryClient repertoryClient;


    @Override
    public void calculateCart(AddCartDTO dto) {

        //查询秒杀商品
        EsMarketingDTO marketingEsDTO = ClientUtil.getDate(esGoodsClient.getMarketingGoodsById(dto.getGoodsId()));

        if (marketingEsDTO == null) {
            Result<String> result = merchantGoodsClient.getGoodsNameFromDbByMgoodsId(dto.getGoodsId());
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, result.getData() + "已下架");
        }

        //验证活动状态
        if (!activityIsOngoing(marketingEsDTO)) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, ErrorCode.ACTIVITY_FREEZE_ERROR_MSG);
        }
        //验证活动商品状态
        if (!goodsIsUpper(marketingEsDTO)) {
            throw new CustomException(ErrorCode.SECKILL_DOWN_ERROR, marketingEsDTO.getGoodsName() + "已下架");
        }

        //不是购物车请求 判断秒杀商品是否已添加到购物车
        if (!dto.getCartRequest()) {

            cartJudge(dto);
        }

        Integer number = dto.getNumber();

        //设置商品状态为上架
        dto.setGoodsStatus(GoodsStatusEnum.UPPER_SHELF.getStatus());
        BigDecimal activityPrice = marketingEsDTO.getActivityPrice();
        //金额为活动价
        dto.setPrice(activityPrice);
        dto.setMarketPrice(marketingEsDTO.getStorePrice());
        dto.setSalesPrice(activityPrice);
        //单次总价
        dto.setTotalPrice(activityPrice.multiply(BigDecimal.valueOf(-number)));
        //秒杀分类id 默认为0
        dto.setCategoryId(CartUtil.SECKILL_CATEGORY_ID);
        //秒杀前端传值为-1
        dto.setNumber(-number);
        //skuCode
        dto.setSkuCode(marketingEsDTO.getSku());

        dto.setPackPrice(marketingEsDTO.getPackPrice());

    }

    private void cartJudge(AddCartDTO dto) {
        //查询是否存在拼单
        CartShareBillBaseDTO billBaseDTO = shareBillClient.getShareBillInfo(dto.getUserId(), dto.getMerchantId(), dto.getShopId());

        CartSeckillNumDTO numDTO = new CartSeckillNumDTO();
        numDTO.setUserId(dto.getUserId());
        numDTO.setShopId(dto.getShopId());
        numDTO.setSeckillEventId(dto.getSeckillEventId());
        numDTO.setGoodsId(dto.getGoodsId());
        numDTO.setShareBillNo(billBaseDTO != null ? billBaseDTO.getShareBillNo() : null);

        //购物车当前秒杀商品是否添加
        Integer num = cartClient.getCartSeckillNum(numDTO);
        if (num > 0) {
            throw new CustomException(ErrorCode.SECKILL_CART_FLAG_ERROR, "购物车中存在该秒杀商品");
        }
        //存在拼单  添加到拼单中
        if (billBaseDTO != null) {
            dto.setShareUserId(billBaseDTO.getShareUserId());
            dto.setShareBillNo(billBaseDTO.getShareBillNo());
            dto.setType(CartTypeEnum.SHARE_BILL.getStatus());
        }
    }

    @Override
    public void cartGoodsCheck(CartGoodsCheckDTO checkDTO, Cart cart) {

//        // 获得活动商品
        EsMarketingDTO seckill = esGoodsClient.getMarketingGoodsById(cart.getGoodsId()).getData();

        if (seckill == null) {
            // 商品被删除的情况
            Result<String> result = merchantGoodsClient.getGoodsNameFromDbByMgoodsId(cart.getGoodsId());
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(result.getData() + "已下架");
            return;
        }


        //3.活动是否下架 / 4.活动是否已结束
        if (!activityIsOngoing(seckill)) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(ErrorCode.ACTIVITY_FREEZE_ERROR_MSG);
        }

        //1.3.0 商品下架 / 菜单移除商品
        if (!goodsIsUpper(seckill)) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(seckill.getGoodsName() + "已下架");
            return;
        }

        //1.3.0 场次是否结束
        MarketingSeckillEventVO eventVO = seckillEventRelationClient.queryByEventId(cart.getSeckillEventId()).getData();
        //当前时间大于场次结束时间
        if (eventVO != null && LocalTime.now().isAfter(eventVO.getEventEndTime())) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(eventVO.getEventBeginTime() + "场已经结束咯");
            return;
        }

        //5.起售数量,限购数量判断 -- [提示用户(活动中不能修改起购,限购,正常情况都是true)]
        if (cart.getNumber() < seckill.getMinQuantity()) {
            checkDTO.addPromptMessage(seckill.getName() + "起售" + seckill.getLimitQuantity());
            return;
        }
        Integer limitQuantity = seckill.getLimitQuantity();
        //-1不限购
        if (!BaseUtil.isNegativeOne(limitQuantity) && cart.getNumber() > limitQuantity) {
            checkDTO.addPromptMessage(seckill.getName() + "限购" + seckill.getLimitQuantity());
            return;
        }

        if (!BaseUtil.priceEquals(cart.getPackPrice(), seckill.getPackPrice())) {
            //重新计算价格
            Cart newCart = recalculatePrice(cart, seckill);
            checkDTO.addRecalculateCart(newCart);
            checkDTO.addPromptMessage(seckill.getName() + ",餐盒费发生变更");
            return;
        }

        //6.库存判断 -- [返回到购物车列表，提示商品剩余库存数量，剩余库存为0,提示xx商品已抢光,清空购物车中的该商品]
        // 获取秒杀库存
        Integer inventory = repertoryClient.getInventoryByOfMGoodsId(cart.getGoodsId(), cart.getSeckillEventId()).getData();
        if (inventory == null || inventory == 0) {
            checkDTO.addDelIds(cart.getId());
            checkDTO.addDelErrMsg(seckill.getGoodsName() + "已抢光");
            return;
        }

        Integer number;
        if (CartUtil.isShareBill(cart.getType())) {
            number = checkDTO.getSkuInventory(CartUtil.seckillKey(cart.getSkuCode(), cart.getSeckillEventId()));
        } else {
            number = cart.getNumber();
        }

        if (inventory < number) {
            checkDTO.addPromptMessage(seckill.getGoodsName() + "当前库存仅剩" + inventory + "份");
        }
    }

    private Cart recalculatePrice(Cart cart, EsMarketingDTO goods) {
        cart.setPackPrice(goods.getPackPrice());
        return cart;
    }

    @Override
    public CartGoodsDTO handlerCartGoods(HandlerCartDTO handlerCartDTO, Cart cart) {

        EsMarketingDTO marketingEsDTO = esGoodsClient.getMarketingGoodsById(cart.getGoodsId()).getData();

        if (marketingEsDTO != null
                && isOngoing(marketingEsDTO)
                && handlerCartDTO.eventIsFinish(cart.getSeckillEventId())
        ) {
            //活动进行中
            CartGoodsDTO cartGoodsDTO = ConvertUtils.sourceToTarget(marketingEsDTO, CartGoodsDTO.class);
            cartGoodsDTO.setSalesPrice(marketingEsDTO.getActivityPrice());
            cartGoodsDTO.setEnterprisePrice(marketingEsDTO.getActivityPrice());
            cartGoodsDTO.setMarketPrice(marketingEsDTO.getStorePrice());
            cartGoodsDTO.setListPicture(marketingEsDTO.getGoodsPicture());
            cartGoodsDTO.setLowestBuy(marketingEsDTO.getMinQuantity());
            cartGoodsDTO.setHighestBuy(marketingEsDTO.getLimitQuantity());
            cartGoodsDTO.setPropertyValue(marketingEsDTO.getSkuValue());
            cartGoodsDTO.setPrice(cart.getPrice());
            cartGoodsDTO.setTotalPrice(cart.getTotalPrice());


            return cartGoodsDTO;
        }
        return null;
    }

    @Override
    public void payOrderGoodsCheck(OrdersDetailDTO order, OrdersDetailGoodsDTO goods) {
        //调用父类检查活动方法
        payOrderGoodsCheckByActivity(goods);
    }

    private Boolean isOngoing(EsMarketingDTO marketingEsDTO) {
        //活动状态
        return activityIsOngoing(marketingEsDTO)
                //活动商品上下架状态
                && goodsIsUpper(marketingEsDTO);
    }

    private Boolean activityIsOngoing(EsMarketingDTO marketingEsDTO) {
        //活动状态
        return Objects.equals(marketingEsDTO.getStatus(), MarketingStatusEnum.ONGOING.getStatus())
                //活动上下架状态
                && Objects.equals(marketingEsDTO.getUpDownState(), GoodsStatusEnum.UPPER_SHELF.getStatus());
    }

    private Boolean goodsIsUpper(EsMarketingDTO marketingEsDTO) {
        //活动商品上下架状态 /  菜单移除商品
        return goodsIsUpper(marketingEsDTO.getGoodsUpDownState(), marketingEsDTO.getDel())
                //售卖渠道是否支持外卖
                && CartUtil.takeout(marketingEsDTO);
    }

    private Boolean goodsIsUpper(Integer goodsStatus, Boolean del) {
        //活动商品上下架状态
        return Objects.equals(goodsStatus, GoodsStatusEnum.UPPER_SHELF.getStatus())
                && !del;
    }


}
