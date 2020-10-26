package com.meiyuan.catering.wx.mq.receive;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.OrderPickSmsMqDTO;
import com.meiyuan.catering.order.enums.DeliveryWayEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.mq.sender.OrderPickSmsMqSender;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryService;
import com.meiyuan.catering.wx.service.marketing.WxMarketingRecommendPrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 处理团购成功消息，更新订单状态
 *
 * @author luohuan
 * @date 2020/4/3
 **/
@Slf4j
@Component
public class WxGroupOrderSuccessMsgReceive {
    @Autowired
    private OrderClient orderClient;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private WxMarketingRecommendPrizeService wxMarketingRecommendPrizeService;

    @Autowired
    private CateringOrdersDeliveryService cateringOrdersDeliveryService;

    @Autowired
    private OrderPickSmsMqSender pickSmsMqSender;

    @RabbitListener(queues = MarketingMqConstant.GROUP_SUCCESS_QUEUE)
    @RabbitHandler
    public void groupOrderSuccess(byte[] receive) {
        String msg = new String(receive, StandardCharsets.UTF_8);
        log.info("接收到团购成功的消息，msg={}", msg);
        try {
            List<String> successOrderNumbs = JSON.parseArray(msg, String.class);
            if (successOrderNumbs != null && !successOrderNumbs.isEmpty()) {
                this.orderClient.upOrderStatus(successOrderNumbs);
                //发送拼团成功短信
                sendShortMsg(successOrderNumbs);
                log.info("团购成功消息处理完成");
            } else {
                log.info("没有订单编号 不处理团购成功消息");
            }
        } catch (Exception e) {
            log.error("团购成功消息处理失败", e);
        }
    }

    /**
     * 处理团购推荐有奖活动和发送拼团成功短信
     *
     * @param successOrderNumbs 订单号列表
     */
    private void sendShortMsg(List<String> successOrderNumbs) {
        successOrderNumbs.forEach(orderNumber -> {
            Result<Order> orderResult = orderClient.getByOrderNumber(orderNumber);
            if (orderResult.success() && orderResult.getData() != null) {
                Order order = orderResult.getData();
                //发送团购成功短信
                OrderDelivery deliveryEntity = cateringOrdersDeliveryService.getByOrderId(order.getId());
                String code = deliveryEntity.getConsigneeCode();
                String storeName = deliveryEntity.getStoreName();
                if (order.getDeliveryWay().equals(DeliveryWayEnum.Delivery.getCode())) {
                    //发送配送短信
                    String[] paramStr = {code,String.valueOf(order.getId())};
                    notifyService.notifySmsTemplate(deliveryEntity.getConsigneePhone(), NotifyType.GROUP_ORDER_HOME_DELIVERY_NOTIFY, paramStr);
                } else if (order.getDeliveryWay().equals(DeliveryWayEnum.invite.getCode())) {
                    //发送自提短信
                    String[] paramStr = {code,storeName,String.valueOf(order.getId())};
                    notifyService.notifySmsTemplate(deliveryEntity.getConsigneePhone(), NotifyType.GROUP_ORDER_SELF_PICKUP_NOTIFY, paramStr);
                    //提前1小时发送聚餐提醒短信
                    sendInviteRemindShortMsg(deliveryEntity);
                }
            }
        });
    }

    /**
     * 发送自提订单提醒短信（提前1小时发送）
     *
     * @param deliveryEntity
     */
    private void sendInviteRemindShortMsg(OrderDelivery deliveryEntity) {
        OrderPickSmsMqDTO smsMqDTO = new OrderPickSmsMqDTO();
        smsMqDTO.setConsigneeCode(deliveryEntity.getConsigneeCode());
        smsMqDTO.setEstimateTime(deliveryEntity.getEstimateTime());
        smsMqDTO.setEstimateEndTime(deliveryEntity.getEstimateEndTime());
        smsMqDTO.setPhone(deliveryEntity.getConsigneePhone());
        smsMqDTO.setCreateTime(deliveryEntity.getCreateTime());
        //下单成功距自提1小时短信通知
        pickSmsMqSender.sendPickSmsPushMsg(smsMqDTO);
    }
}
