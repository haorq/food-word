package com.meiyuan.catering.wx.utils.support;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.exception.CustomException;
import com.meiyuan.catering.core.exception.ErrorCode;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.ConvertUtils;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailBaseWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailGoodsWxDTO;
import com.meiyuan.catering.order.dto.query.wx.OrdersDetailWxDTO;
import com.meiyuan.catering.order.enums.OrderStatusEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.user.dto.cart.CartDTO;
import com.meiyuan.catering.user.dto.cart.CartGoodsDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillDTO;
import com.meiyuan.catering.user.dto.cart.CartShareBillGoodsDTO;
import com.meiyuan.catering.user.dto.sharebill.CreateShareBillDTO;
import com.meiyuan.catering.user.dto.sharebill.ShareBillQueryDTO;
import com.meiyuan.catering.user.enums.BillStatusEnum;
import com.meiyuan.catering.user.fegin.cart.CartClient;
import com.meiyuan.catering.user.fegin.sharebill.CartShareBillClient;
import com.meiyuan.catering.wx.dto.UserTokenDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxCartShareBillInfoDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxCartShareBillUserDTO;
import com.meiyuan.catering.wx.dto.sharebill.WxShareBillRefreshDTO;
import com.meiyuan.catering.wx.utils.WechatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zengzhangni
 * @date 2020/6/22 10:59
 * @since v1.1.0
 */
@Component
public class ShareBillSupport {

    @Autowired
    private ThreadPoolTaskExecutor shareBillExecutor;
    @Autowired
    private CartShareBillClient cartShareBillClient;
    @Autowired
    private WechatUtils wechatUtils;
    @Autowired
    private CartClient cartClient;
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private MerchantUtils merchantUtils;
    @Autowired
    private CartSupport cartSupport;


    public void userTypeVerify(UserTokenDTO token) {
        if (token.isCompanyUser()) {
            throw new CustomException(ErrorCode.SHARE_BILL_USER_TYPE_ERROR, "您是企业用户，不可以对拼单进行操作哦~~~~");
        }
    }


    public void copyProperty(UserTokenDTO token, CreateShareBillDTO dto) {
        dto.setShareUserId(token.getUserId());
        dto.setAvatar(token.getAvatar());
        dto.setNickname(token.getNickname());
    }

    public Result<String> create(CreateShareBillDTO dto) {
        return cartShareBillClient.create(dto);
    }

    public void cartTypeToShareBill(Long merchantId, Long shopId, String shareBillNo, Long shareUserId) {
        cartClient.cartTypeToShareBill(merchantId, shopId, shareBillNo, shareUserId);
    }


    public void cacheUpdate(String shareBillNo) {
        //更新缓存
        shareBillExecutor.execute(() -> cartSupport.shareBillCartListAndCache(shareBillNo));
    }

    public Result cancel(String shareBillNo, Long userId) {
        cartShareBillClient.cancel(shareBillNo, userId);

        return Result.succ();
    }

    public void cartTypeToGoods(String shareBillNo) {
        cartClient.cartTypeToGoods(shareBillNo);
    }

    public void cacheDelete(String shareBillNo) {
        wechatUtils.clearCacheShareBillCartInfo(shareBillNo);
    }

    public Result exist(String shareBillNo, Long userId) {
        cartShareBillClient.exist(shareBillNo, userId);
        return Result.succ();
    }


    public Result<WxShareBillRefreshDTO> refresh(UserTokenDTO token, String shareBillNo) {

        WxShareBillRefreshDTO wxShareBillRefreshDTO = new WxShareBillRefreshDTO();
        wxShareBillRefreshDTO.setCancelBill(isCancelBill(shareBillNo));

        CartDTO cartDTO = wechatUtils.getShareBillCartInfo(shareBillNo);

        wxShareBillRefreshDTO.setCart(cartDTO);
        return Result.succ(wxShareBillRefreshDTO);
    }

    private boolean isCancelBill(String shareBillNo) {
        return wechatUtils.readShareBillCancel(shareBillNo);
    }

    public Result returnChooseGoods(String shareBillNo) {
        return cartShareBillClient.returnChooseGoods(shareBillNo);
    }

    public Result<WxCartShareBillInfoDTO> detail(UserTokenDTO token, String shareBillNo) {
        Result<WxCartShareBillInfoDTO> result = shareBillInfo(shareBillNo);
        if (result.getData() == null) {
            return Result.fail("来晚啦，拼单已经结束咯~~~~");
        }
        WxCartShareBillInfoDTO dto = result.getData();

        // 状态：1--完成选购，2--提交订单,3--完成拼单
        int billStatus = BillStatusEnum.CHOOSING.getStatus();

        // 拼单人--购物车查询商品信息
        // 1、已提交订单---订单中查
        if (Objects.equals(dto.getStatus(), BillStatusEnum.PAYED.getStatus())) {
            // 2.1 未支付-发起人--订单查询详情
            OrdersDetailWxDTO ordersDto = orderClient.orderDetailQueryWx(dto.getOrderId(), token.getUserId()).getData();
            billStatus = orderDetailToShareBillDetail(dto, ordersDto);
        }
        // 2、未提交订单--购物车中查
        else {

            CartDTO cartDTO = cartSupport.listCartShareBillGoods130(shareBillNo, token.getUserId());
            List<CartShareBillGoodsDTO> cartShareBillGoodsDtoList = listValidCartShareBillGoodsDtoList(cartDTO.getShareBillGoodsList());
            dto.setShareBillGoodsList(cartShareBillGoodsDtoList);

            dto.setDeliveryPrice(BigDecimal.ZERO);
            dto.setDiscountFee(BigDecimal.ZERO);
            dto.setTotalPackAmt(cartDTO.getTotalPackAmt());
            dto.setOrderAmount(cartDTO.getTotalAmt());
        }

        dto.setDeliveryWay(merchantUtils.getShopConfigInfo(dto.getShopId()).getBusinessSupport());
        ShopInfoDTO shop = merchantUtils.getShop(dto.getShopId());
        if (shop == null) {
            throw new CustomException(ErrorCode.SHOP_STATUS_ERROR, ErrorCode.SHOP_STATUS_MSG);
        }
        dto.setStoreName(shop.getShopName());
        dto.setBillStatus(billStatus);
        return Result.succ(dto);
    }

    /**
     * 获取有效拼单列表（过滤掉goodList为空的数据）
     *
     * @param shareBillGoodsList
     * @return
     */
    private List<CartShareBillGoodsDTO> listValidCartShareBillGoodsDtoList(List<CartShareBillGoodsDTO> shareBillGoodsList) {
        return shareBillGoodsList.stream()
                .filter(billGoods -> billGoods.getGoodsList() != null && !billGoods.getGoodsList().isEmpty())
                .collect(Collectors.toList());
    }

    private CartGoodsDTO getCartGoodsDTO(OrdersDetailGoodsWxDTO orderGood) {
        CartGoodsDTO goods = new CartGoodsDTO();
        goods.setUserId(orderGood.getShareBillUserId());
        goods.setGoodsName(orderGood.getGoodsName());
        goods.setListPicture(orderGood.getGoodsPicture());
        goods.setNumber(orderGood.getQuantity());
        goods.setPrice(orderGood.getSalesPrice());
        goods.setMarketPrice(orderGood.getStorePrice());
        goods.setPropertyValue(orderGood.getGoodsSpecificationDesc());
        goods.setTotalPrice(orderGood.getDiscountLaterFee());
        goods.setListPicture(orderGood.getGoodsPicture());
        goods.setInfoPicture(orderGood.getGoodsPicture());
        goods.setGoodsType(orderGood.getGoodsType());
        return goods;
    }

    private int orderDetailToShareBillDetail(WxCartShareBillInfoDTO dto, OrdersDetailWxDTO ordersDto) {

        OrdersDetailBaseWxDTO orders = ordersDto.getBase();


        List<OrdersDetailGoodsWxDTO> orderGoods = ordersDto.getGoods();

        //赠品
        List<CartGoodsDTO> giftList = orderGoods.stream().filter(OrdersDetailGoodsWxDTO::getGifts).map(this::getCartGoodsDTO).collect(Collectors.toList());
        dto.setGiftList(giftList);

        //商品分组
        Map<Long, List<CartGoodsDTO>> goodsMap = orderGoods.stream().filter(e -> !e.getGifts()).map(this::getCartGoodsDTO).collect(Collectors.groupingBy(CartGoodsDTO::getUserId));


        List<CartShareBillGoodsDTO> shareBillUserDtoList = dto.getUserList().stream().map(user -> {
            CartShareBillGoodsDTO shareBillUserDTO = BaseUtil.objToObj(user, CartShareBillGoodsDTO.class);

            //获取自己选择的商品
            List<CartGoodsDTO> cartGoodsDtoList = goodsMap.get(shareBillUserDTO.getUserId());
            if (cartGoodsDtoList != null && cartGoodsDtoList.size() > 0) {
                //拼单人选购总价
                double sum = cartGoodsDtoList.stream().mapToDouble(e -> e.getTotalPrice().doubleValue()).sum();
                shareBillUserDTO.setTotalAmt(new BigDecimal(sum));

                //拼单商品数据
                shareBillUserDTO.setGoodsList(cartGoodsDtoList);
            }

            return shareBillUserDTO;
        }).collect(Collectors.toList());


        //过滤没有选择商品的数据
        dto.setShareBillGoodsList(listValidCartShareBillGoodsDtoList(shareBillUserDtoList));

        OrdersDetailBaseWxDTO base = ordersDto.getBase();
        dto.setDeliveryPrice(base.getDeliveryPrice());
        dto.setDiscountFee(base.getDiscountFee());
        dto.setOrderAmount(base.getOrderAmount());
        dto.setTotalPackAmt(base.getPackPrice());

        return Objects.equals(orders.getOrderStatus(), OrderStatusEnum.UNPAID.getValue()) ?
                BillStatusEnum.PAYING.getStatus() :
                BillStatusEnum.PAYED.getStatus();

    }


    /**
     * @param shareBillNo 拼单号
     * @return {@link Result<WxCartShareBillInfoDTO>}
     * @description 拼单信息
     * @author yaozou
     * @date 2020/3/31 18:21
     * @since v1.0.0
     */
    private Result<WxCartShareBillInfoDTO> shareBillInfo(String shareBillNo) {
        CartShareBillDTO billDTO = cartShareBillClient.getShareBill(new ShareBillQueryDTO(shareBillNo, true)).getData();
        if (billDTO.getShareBill() == null) {
            return Result.succ();
        }
        WxCartShareBillInfoDTO dto = ConvertUtils.sourceToTarget(billDTO.getShareBill(), WxCartShareBillInfoDTO.class);

        List<WxCartShareBillUserDTO> userList = new ArrayList<>(billDTO.getShareBillUserList().size());
        billDTO.getShareBillUserList().forEach(userEntity -> {
            userList.add(ConvertUtils.sourceToTarget(userEntity, WxCartShareBillUserDTO.class));
        });
        dto.setUserList(userList);
        return Result.succ(dto);
    }

    public void sendShareBillCancel(String shareBillNo) {
        wechatUtils.sendShareBillCancel(shareBillNo);
    }

    public void existDelGoods(String shareBillNo, Long userId) {
        cartClient.existDelGoods(shareBillNo, userId);
    }

    public void cancelDelGoods(String shareBillNo, Long userId) {
        cartClient.cancelDelGoods(shareBillNo, userId);
    }
}
