package com.meiyuan.catering.pay.util;

import com.meiyuan.catering.core.dto.base.ShopInfoDTO;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.dto.pay.RefundOrder;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.BaseUtil;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.marketing.dto.pickup.GiftUpdateStockDTO;
import com.meiyuan.catering.marketing.feign.MarketingPickupPointClient;
import com.meiyuan.catering.marketing.feign.MarketingPullNewClient;
import com.meiyuan.catering.marketing.feign.UserTicketClient;
import com.meiyuan.catering.merchant.goods.fegin.ShopGoodsClient;
import com.meiyuan.catering.merchant.utils.MerchantUtils;
import com.meiyuan.catering.order.dto.OrderSecKillMqDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailDTO;
import com.meiyuan.catering.order.dto.query.OrdersDetailGoodsDTO;
import com.meiyuan.catering.order.entity.CateringOrdersDiscountsEntity;
import com.meiyuan.catering.order.enums.*;
import com.meiyuan.catering.order.feign.*;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.service.MyPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 描述: 退款数据处理
 *
 * @author zengzhangni
 * @date 2020/4/1 10:38
 */
@Slf4j
@Component
public class RefundSupport {
    @Autowired
    private OrderRefundClient refundClient;
    @Autowired
    private OrderRefundAuditClient refundAuditClient;
    @Resource
    private RefundOrderOperationClient refundOperationClient;
    @Resource
    private OrderOperationClient operationClient;
    @Resource
    private OrderClient orderClient;
    @Autowired
    private MarketingPickupPointClient marketingPickupPointClient;
    @Autowired
    private OrderDiscountsClient discountsClient;
    @Autowired
    private UserTicketClient userTicketClient;
    @Autowired
    private OrderMqSenderClient senderClient;

    @Resource
    private MerchantUtils merchantUtils;
    @Resource
    private NotifyService notifyService;
    @Resource
    private ShopGoodsClient shopGoodsClient;
    @Resource
    private MarketingPullNewClient pullNewClient;


    /**
     * 描述: 退款实现
     *
     * @param payWay
     * @param refundOrder
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 11:09
     */
    public void refundImpl(PayEnum payWay, RefundOrder refundOrder) {
        MyPayService payService = PayContext.getPayService(payWay);
        payService.refund(refundOrder);
    }

    /**
     * 描述: 更新退款审核
     *
     * @param shopId
     * @param refundId
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 10:49
     */
    public void updateById(Long shopId, Long refundId, AuditStatusEnum auditStatusEnum, String auditExplain) {
        ShopInfoDTO shop = merchantUtils.getShop(shopId);
        refundAuditClient.updateAudit(shopId, refundId, auditStatusEnum, auditExplain, shop.getShopName());
    }

    /**
     * 描述:添加退款进度
     *
     * @param orderId
     * @param refundId
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 10:49
     */
    public void saveRefundOperation(Long orderId, Long refundId, RefundOperationEnum operationEnum, String remarks) {
        refundOperationClient.saveOperation(orderId, refundId, operationEnum, remarks);
    }

    public void saveRefundOperation(Long orderId, Long refundId, RefundOperationEnum operationEnum) {
        saveRefundOperation(orderId, refundId, operationEnum, "");
    }

    public void delPullNew(Long orderId) {
        pullNewClient.delPullNew(orderId);
    }

    /**
     * 描述:修改退款状态
     *
     * @param refundId
     * @param refundStatusEnum
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 11:11
     */
    public void updateRefundStatus(Long refundId, RefundStatusEnum refundStatusEnum) {
        refundClient.updateRefundStatus(refundId, refundStatusEnum);
    }


    /**
     * 描述: 更新订单状态为取消
     *
     * @param orderId
     * @return void
     * @author zengzhangni
     * @date 2020/4/1 15:29
     */
    public void updateStatusToCanceled(Long orderId, String offReason) {
        orderClient.updateStatusToCanceled(orderId, offReason, OrderOffTypeEnum.EMPLOYEE_CANCEL);
    }


    public void cancelImpl(PayEnum payWay, Order order) {
        MyPayService payService = PayContext.getPayService(payWay);
        payService.cancel(order);
    }

    public void notifySms(Order order) {
        LocalDateTime billingTime = order.getBillingTime();
        String dateTimeDisplayString = DateTimeUtil.getDateTimeDisplayString(billingTime);
        String[] paramsStr = new String[]{dateTimeDisplayString};
        notifyService.notifySmsTemplate(order.getMemberPhone(), NotifyType.MERCHANT_CANCEL_ORDER_NOTIFY, paramsStr);
    }

    /**
     * 描述:添加订单进度
     *
     * @param order
     * @param operatorDesc 操作说明 v1.3.0 订单取消原因记录
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 16:08
     */
    @Async
    public void saveOrderOperation(Order order, String operatorDesc) {
        operationClient.saveOperation(order, operatorDesc, OrderOperationEnum.CANCELED, OrderOperationTypeEnum.MERCHANT);
        operationClient.saveOperation(order, operatorDesc, OrderOperationEnum.REFUND, OrderOperationTypeEnum.MERCHANT);
    }


    /**
     * 描述:处理商品库存
     *
     * @param order
     * @return void
     * @author zengzhangni
     * @date 2020/4/10 16:08
     */
    @Async
    @SuppressWarnings("all")
    public void goodsInventoryHandle(Long orderId) {
        OrdersDetailDTO ordersDetailDTO = orderClient.orderDetail(orderId);
        if (ordersDetailDTO != null) {
            List<OrdersDetailGoodsDTO> goods = ordersDetailDTO.getGoods();
            // 还原赠送商品库存
            List<OrdersDetailGoodsDTO> giftGoods = goods.stream().filter(OrdersDetailGoodsDTO::getGifts).collect(Collectors.toList());
            Long shopId = ordersDetailDTO.getStoreId();
            if (BaseUtil.judgeList(giftGoods)) {
                List<GiftUpdateStockDTO> collect = giftGoods.stream().map(e -> {
                    GiftUpdateStockDTO giftUpdateStockDTO = new GiftUpdateStockDTO();
                    giftUpdateStockDTO.setPickupId(e.getGiftsActivityId());
                    giftUpdateStockDTO.setNumber(e.getQuantity());
                    return giftUpdateStockDTO;
                }).collect(Collectors.toList());
                log.debug("还原赠送商品库存");
                this.marketingPickupPointClient.updateShopGiftStock(collect, shopId, false);
            }
            // 还原秒杀商品库存
            List<OrdersDetailGoodsDTO> secGoods = goods.stream().filter(e -> OrderGoodsTypeEnum.SECONDS.getValue().equals(e.getGoodsType()))
                    .collect(Collectors.toList());
            if (BaseUtil.judgeList(secGoods)) {
                List<OrderSecKillMqDTO> secKillMqDTOList = secGoods.stream().map(obj -> {
                    OrderSecKillMqDTO orderSecKillMqDTO = new OrderSecKillMqDTO();
                    orderSecKillMqDTO.setGoodsId(obj.getGoodsId());
                    orderSecKillMqDTO.setNumber(obj.getQuantity());
                    orderSecKillMqDTO.setUserId(ordersDetailDTO.getMemberId());
                    orderSecKillMqDTO.setOrder(Boolean.TRUE);
                    orderSecKillMqDTO.setSeckillEventId(obj.getSeckillEventId());
                    return orderSecKillMqDTO;
                }).collect(Collectors.toList());
                if (BaseUtil.judgeList(secKillMqDTOList)) {
                    log.debug("发送取消订单还原秒杀商品库存消息");
                    this.senderClient.sendSeckillMsg(secKillMqDTOList);
                }
            }
            // 获取用户优惠卷信息，并还原优惠卷
//            Long discountId = discountsClient.getDiscountId(ordersDetailDTO.getOrderId()).getData();
//            if (discountId != null) {
//                userTicketClient.returnTicket(discountId);
//            }
            // 优惠券支持2张 v1.3.0 lh
            List<CateringOrdersDiscountsEntity> orderTicketList = discountsClient.list(ordersDetailDTO.getOrderId()).getData();
            if (!CollectionUtils.isEmpty(orderTicketList)) {
                for (CateringOrdersDiscountsEntity entity : orderTicketList) {
                    userTicketClient.returnTicket(entity.getDiscountId());
                }
            }

            // 普通/特价商品库存还原
            List<OrdersDetailGoodsDTO> ordinaryGoodsList = goods
                    .stream()
                    //过滤赠品
                    .filter(i -> !i.getGifts())
                    .filter(i -> i.getGoodsType().equals(OrderGoodsTypeEnum.ORDINARY.getValue())
                            || i.getGoodsType().equals(OrderGoodsTypeEnum.SPECIAL.getValue()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(ordinaryGoodsList)) {
                ConcurrentHashMap<String/* 商品SKU编码 */, Integer/* 订单商品数量 */> skuMap = new ConcurrentHashMap<>(16);

                /* 拼单，同一个商品SKU，会有多人购买 */
                for (OrdersDetailGoodsDTO dto : ordinaryGoodsList) {
                    if (skuMap.containsKey(dto.getGoodsSkuCode())) {
                        skuMap.put(dto.getGoodsSkuCode(), skuMap.get(dto.getGoodsSkuCode()) + dto.getQuantity());
                    }
                    skuMap.put(dto.getGoodsSkuCode(), dto.getQuantity());
                }

                for (String skuCode : skuMap.keySet()) {
                    /*库存做减法，这里将数量乘以-1*/
                    skuMap.put(skuCode, skuMap.get(skuCode));
                }
                shopGoodsClient.batchUpdateSkuStock(shopId, skuMap);
            }
        }
    }

    /**
     * 描述:通过订单id查询订单
     *
     * @param orderId
     * @return com.meiyuan.catering.core.dto.pay.Order
     * @author zengzhangni
     * @date 2020/5/20 10:46
     * @since v1.1.0
     */
    public Order getOrderById(Long orderId) {
        return orderClient.getOrderById(orderId).getData();
    }

    /**
     * 描述:通过id查询退款订单
     *
     * @param refundId
     * @return com.meiyuan.catering.core.dto.pay.RefundOrder
     * @author zengzhangni
     * @date 2020/5/20 10:46
     * @since v1.1.0
     */
    public RefundOrder getRefundOrderById(Long refundId) {
        return refundClient.getRefundOrderById(refundId).getData();
    }
}
