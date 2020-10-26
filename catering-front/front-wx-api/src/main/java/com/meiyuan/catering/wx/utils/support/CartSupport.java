package com.meiyuan.catering.wx.utils.support;

import com.meiyuan.catering.core.dto.cart.Cart;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.marketing.feign.MarketingSeckillEventRelationClient;
import com.meiyuan.catering.user.dto.cart.*;
import com.meiyuan.catering.user.fegin.cart.CartClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillUserClient;
import com.meiyuan.catering.user.utils.CartUtil;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import com.meiyuan.catering.wx.utils.strategy.CartContext;
import com.meiyuan.catering.wx.utils.strategy.dto.CartGoodsCheckDTO;
import com.meiyuan.catering.wx.utils.strategy.dto.HandlerCartDTO;
import com.meiyuan.marsh.concurrent.support.LockTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020/6/22 10:38
 * @since v1.1.0
 */
@Component
public class CartSupport {

    @Autowired
    private ThreadPoolTaskExecutor cartExecutor;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private CartShareBillClient cartShareBillClient;
    @Autowired
    private CartShareBillUserClient cartShareBillUserClient;
    @Autowired
    private MarketingSeckillEventRelationClient seckillEventRelationClient;
    @Autowired
    private WechatUtils wechatUtils;
    @Autowired
    private LockTemplate<Long, Result> template;

    public Result add(AddCartDTO cartDTO) {
        return cartClient.add(cartDTO);
    }

    public Result clear(ClearCartDTO cartDTO) {
        return cartClient.clear(cartDTO);
    }

    public Result cartGoodsCheck(CartMerchantDTO merchantDTO) {

        //查询购物车
        List<Cart> carts = cartClient.listCheckCart(merchantDTO);

        //保存检查信息
        CartGoodsCheckDTO checkDTO = new CartGoodsCheckDTO();

        //检查商品之前处理数据
        beforeDataDispose(merchantDTO, checkDTO, carts);

        //循环检查商品
        for (Cart cart : carts) {
            CartContext cartContext = new CartContext(cart.getGoodsType());
            cartContext.cartGoodsCheckByDefault(checkDTO, cart);
        }

        //购物车检查处理
        cartCheckDispose(checkDTO, merchantDTO);

        return Result.succ();
    }

    private void beforeDataDispose(CartMerchantDTO merchantDTO, CartGoodsCheckDTO checkDTO, List<Cart> carts) {
        //todo 优化处理批量拉取数据 (商品信息,库存信息,秒杀场次信息)

        //拼单合并sku 选中数量,验证库存
        shareBillInventory(merchantDTO, checkDTO, carts);

    }

    private void shareBillInventory(CartMerchantDTO merchantDTO, CartGoodsCheckDTO checkDTO, List<Cart> carts) {
        if (CartUtil.isShareBill(merchantDTO.getShareBillNo())) {
            //普通商品
            List<Cart> ordinaryGoods = carts.stream().filter(cart -> CartUtil.isOrdinaryGoods(cart.getGoodsType())).collect(Collectors.toList());
            //秒杀商品
            List<Cart> seckillGoods = carts.stream().filter(cart -> CartUtil.isSeckillGoods(cart.getGoodsType())).collect(Collectors.toList());

            //普通 sku->库存
            Map<String, Integer> ordinarySkuInventoryMap = ordinaryGoods.stream().collect(Collectors.toMap(Cart::getSkuCode, Cart::getNumber, (oldValue, newValue) -> oldValue + newValue));
            //秒杀 sku+场次id  -> 库存
            Map<String, Integer> seckillSkuInventoryMap = seckillGoods.stream().collect(Collectors.toMap(cart -> CartUtil.seckillKey(cart.getSkuCode(), cart.getSeckillEventId()), Cart::getNumber, (oldValue, newValue) -> oldValue + newValue));

            //合并
            ordinarySkuInventoryMap.putAll(seckillSkuInventoryMap);

            checkDTO.setSkuInventoryMap(ordinarySkuInventoryMap);
        }
    }


    /**
     * 描述:处理购物车检查返回结果
     *
     * @param checkDTO
     * @param merchantDTO
     * @return void
     * @author zengzhangni
     * @date 2020/7/6 16:48
     * @since v1.2.0
     */
    private void cartCheckDispose(CartGoodsCheckDTO checkDTO, CartMerchantDTO merchantDTO) {
        Set<Long> delIds = checkDTO.getDelIds();
        Set<Cart> recalculateCart = checkDTO.getRecalculateCart();
        Set<String> delErrMsg = checkDTO.getDelErrMsg();
        Set<String> promptMessage = checkDTO.getPromptMessage();
        Set<String> recalculateCartErrMsg = checkDTO.getRecalculateCartErrMsg();

        if (delIds.size() > 0) {
            cartClient.removeByIds(delIds);
        }


        if (recalculateCart.size() > 0) {
            //批量更新计算后的购物车
            cartClient.updateCarts(recalculateCart);
        }

        StringBuilder errMsgBuilder = new StringBuilder();

        //错误信息 拼接错误信息 <p></p> 是为了前端换行
        if (delErrMsg.size() > 0) {
            for (String s : delErrMsg) {
                errMsgBuilder.append(s).append(";<p></p>");
            }
        }
        //提示信息 拼接提示信息
        if (promptMessage.size() > 0) {
            for (String s : promptMessage) {
                errMsgBuilder.append(s).append(";<p></p>");
            }
        }
        //价格,规格改变提示信息 拼接提示信息
        if (recalculateCartErrMsg.size() > 0) {
            for (String s : recalculateCartErrMsg) {
                errMsgBuilder.append(s).append(";<p></p>");
            }
        }

        if (StringUtils.isNotBlank(errMsgBuilder)) {
            throw new CustomException(ErrorCode.CART_CHECK_ERROR, errMsgBuilder.toString());
        }
    }


    /**
     * 描述:计算/验证购物车
     *
     * @param cartDTO
     * @return void
     * @author zengzhangni
     * @date 2020/6/22 9:55
     * @since v1.1.0
     */
    public void calculateCart(AddCartDTO cartDTO) {
        CartContext cartContext = new CartContext(cartDTO.getGoodsType());
        cartContext.calculateCart(cartDTO);
    }


    /**
     * 描述:更新拼单缓存信息
     *
     * @param result
     * @param cartDTO
     * @return void
     * @author zengzhangni
     * @date 2020/6/22 10:06
     * @since v1.1.0
     */
    public void cacheUpdate(Result result, AddCartDTO cartDTO) {
        if (result.success()) {
            //如果是拼单状态,需要更新缓存
            if (CartUtil.isShareBill(cartDTO.getType())) {
                cartExecutor.execute(() -> shareBillCartListAndCache(cartDTO.getShareBillNo(), cartDTO.getUserId()));
            }
        }
    }

    public void cacheUpdate(Result result, ClearCartDTO cartDTO) {
        if (result.success()) {
            //如果是拼单状态,需要更新缓存
            if (CartUtil.isShareBill(cartDTO.getType())) {
                cartExecutor.execute(() -> shareBillCartListAndCache(cartDTO.getShareBillNo(), cartDTO.getUserId()));
            }
        }
    }

    /**
     * 描述:验证拼单状态
     *
     * @param type
     * @param shareBillNo
     * @param userId
     * @return void
     * @author zengzhangni
     * @date 2020/6/22 10:06
     * @since v1.1.0
     */
    public void shareBillStatusVerify(Integer type, String shareBillNo, Long userId) {
        if (CartUtil.isShareBill(type)) {
            cartShareBillClient.shareBillStatusVerify(shareBillNo, userId);
        }
    }

    public void shareBillStatusVerify(AddCartDTO cartDTO, Long userId) {
        if (CartUtil.isShareBill(cartDTO.getType())) {
            Long shareUserId = cartShareBillClient.shareBillStatusVerify(cartDTO.getShareBillNo(), userId);
            cartDTO.setShareUserId(shareUserId);
        }
    }

    /**
     * 描述:复制属性
     *
     * @param token
     * @param cartDTO
     * @return void
     * @author zengzhangni
     * @date 2020/6/22 10:07
     * @since v1.1.0
     */
    public void copyProperty(UserTokenDTO token, AddCartDTO cartDTO) {
        cartDTO.setUserId(token.getUserId());
        cartDTO.setUserType(token.getUserType());
    }

    public void copyProperty(UserTokenDTO token, ClearCartDTO cartDTO) {
        cartDTO.setUserId(token.getUserId());
        cartDTO.setUserType(token.getUserType());
    }

    public void cartVerify(CartGoodsQueryDTO queryDTO, UserTokenDTO token) {
        if (CartUtil.isShareBill(queryDTO.getType())) {
            if (token.isCompanyUser()) {
                throw new CustomException(ErrorCode.SHARE_BILL_USER_TYPE_ERROR, "您是企业用户，不可以进行拼单哦~~~~");
            }
            if (StringUtils.isBlank(queryDTO.getShareBillNo())) {
                throw new CustomException("shareBillNo不能为空");
            }
        } else {
            if (queryDTO.getMerchantId() == null) {
                throw new CustomException("merchantId不能为空");
            }
        }
    }

    public Result lock(Long userId, Function<Long, Result> action, Function<Long, Result> alternativeAction) {
        return template.tryLock(userId, action, alternativeAction);
    }

    /**
     * @param
     * @return
     * @description 购物车列表
     * @author yaozou
     * @date 2020/5/19 16:47
     * @since v1.0.0
     */
    public Result<CartDTO> listCartGoods(CartGoodsQueryDTO queryDTO) {
        return Result.succ(handlerCartGoods(cartClient.listEntity(queryDTO), queryDTO.getUserId()));
    }


    public Result join(String shareBillNo, Long userId, String avatar, String nickname) {
        //验证拼单状态
        cartShareBillClient.shareBillStatusVerify(shareBillNo, userId);

        JoinShareBillDTO dto = new JoinShareBillDTO();
        dto.setShareBillNo(shareBillNo);
        dto.setUserId(userId);
        dto.setAvatar(avatar);
        dto.setNickname(nickname);
        return cartShareBillClient.join(dto);
    }


    /**
     * @param list
     * @return {@link CartDTO}
     * @description 处理购物车商品信息
     * @author yaozou
     * @date 2020/3/26 12:57
     * @since v1.0.0
     */
    public CartDTO handlerCartGoods(List<Cart> list, Long userId) {

        HandlerCartDTO handlerCartDTO = new HandlerCartDTO();

        //计算购物车数据
        if (list != null && list.size() > 0) {

            //循环检查商品之前 处理数据
            beforeHandlerCart(list, handlerCartDTO, userId);

            list.forEach(cart -> {
                CartContext cartContext = new CartContext(cart.getGoodsType());
                cartContext.handlerCartGoods(handlerCartDTO, cart);
            });
        }

        Set<Long> deleteCartIds = handlerCartDTO.getDeleteCartIds();

        if (deleteCartIds.size() > 0) {
            cartClient.removeByIds(deleteCartIds);
        }

        //组装返回数据
        CartDTO cartDTO = new CartDTO();
        cartDTO.setGoodsList(handlerCartDTO.getResult());
        cartDTO.setGoodsNum(handlerCartDTO.getGoodsNum().get());
        cartDTO.setTotalAmt(handlerCartDTO.getTotalAmt());
        cartDTO.setTotalOldAmt(handlerCartDTO.getTotalOldAmt());
        cartDTO.setTotalPackAmt(handlerCartDTO.getTotalPackAmt());
        return cartDTO;
    }

    public void priceChangeResultFail(Result result, String goodsName) {
        if (result.success()) {
            Object cartId = result.getData();
            if (Objects.equals(cartId, BaseUtil.CHANGE_FLAG)) {
                result.setCode(ErrorCode.CART_GOODS_CHANGED_ERROR);
                result.setMsg(goodsName + "已变更价格");
            }
        }
    }

    public Result<CartDTO> shareBillCartListAndCache(String shareBillNo) {
        return shareBillCartListAndCache(shareBillNo, null);
    }

    public Result<CartDTO> shareBillCartListAndCache(String shareBillNo, Long userId) {
        CartDTO cartDTO = listCartShareBillGoods130(shareBillNo, userId);
        wechatUtils.cacheShareBillCartInfo(shareBillNo, cartDTO);
        return Result.succ(cartDTO);
    }


    /**
     * 描述:拼单购物车列表
     *
     * @param shareBillNo
     * @return com.meiyuan.catering.user.dto.cart.CartDTO
     * @author zengzhangni
     * @date 2020/8/5 11:38
     * @since v1.3.0
     */
    public CartDTO listCartShareBillGoods130(String shareBillNo, Long userId) {

        List<Cart> carts = cartClient.queryByShareBillNo(shareBillNo);
        CartDTO cartDTO = handlerCartGoods(carts, userId);

        //商品分组 userId -> goodsList
        Map<Long, List<CartGoodsDTO>> goodsMap = cartDTO.getGoodsList().stream().collect(Collectors.groupingBy(CartGoodsDTO::getUserId));

        //查询所有拼单人
        List<CartShareBillUserDTO> billUserDtoList = cartShareBillUserClient.list(shareBillNo).getData();

        //组装返回数据
        List<CartShareBillGoodsDTO> cartShareBillGoodsDtoList = billUserDtoList.stream().map(billUser -> {

            CartShareBillGoodsDTO cartShareBillGoodsDTO = BaseUtil.objToObj(billUser, CartShareBillGoodsDTO.class);
            //从商品map中获取自己的商品数据
            List<CartGoodsDTO> cartGoodsDtoList = goodsMap.get(cartShareBillGoodsDTO.getUserId());
            if (cartGoodsDtoList != null) {
                //购买商品总价
                double sum = cartGoodsDtoList.stream().mapToDouble(e -> e.getTotalPrice().doubleValue()).sum();
                cartShareBillGoodsDTO.setTotalAmt(new BigDecimal(sum));
                //填充每一个拼单人的商品数据
                cartShareBillGoodsDTO.setGoodsList(cartGoodsDtoList);
            }

            return cartShareBillGoodsDTO;

        }).collect(Collectors.toList());


        //清空普通商品数据
        cartDTO.setGoodsList(null);
        //添加拼单商品数据
        cartDTO.setShareBillGoodsList(cartShareBillGoodsDtoList);

        return cartDTO;
    }


    private void beforeHandlerCart(List<Cart> list, HandlerCartDTO handlerCartDTO, Long userId) {
        //设置结束的秒杀场次,秒杀场次过滤用到
        setFinishEventIds(list, handlerCartDTO);

        //设置当前登录人的id 处理价格会用到
        handlerCartDTO.setUserId(userId);
    }


    private void setFinishEventIds(List<Cart> list, HandlerCartDTO handlerCartDTO) {
        //秒杀商品场次id集合
        List<Long> seckillEventIds = list.stream().filter(
                cart -> CartUtil.isSeckillGoods(cart.getGoodsType())
                        && cart.getSeckillEventId() != null
        ).map(Cart::getSeckillEventId).collect(Collectors.toList());

        if (seckillEventIds.size() > 0) {
            //查询进行中的场次id
            List<Long> underwayEventIds = seckillEventRelationClient.underwayEventIdList(seckillEventIds).getData();
            //剩下的就是已结束的场次 需要删除
            seckillEventIds.removeAll(underwayEventIds);

            //保存起来
            handlerCartDTO.setFinishEventIds(seckillEventIds);
        }
    }
}
