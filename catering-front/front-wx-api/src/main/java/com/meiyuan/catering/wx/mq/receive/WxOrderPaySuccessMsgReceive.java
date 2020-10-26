package com.meiyuan.catering.wx.mq.receive;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.dto.base.PaySuccessNotifyDTO;
import com.meiyuan.catering.order.entity.CateringOrdersDeliveryNoEntity;
import com.meiyuan.catering.order.enums.DeliveryRemarkEnum;
import com.meiyuan.catering.order.enums.OrderTypeEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.pay.util.AllinHelper;
import com.meiyuan.catering.wx.service.marketing.WxMarketingGrouponService;
import com.meiyuan.catering.wx.service.marketing.WxMarketingRecommendPrizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author luohuan
 * @date 2020/4/1
 **/
@Slf4j
@Component
public class WxOrderPaySuccessMsgReceive {

    @Autowired
    private WxMarketingGrouponService wxMarketingGrouponService;

    @Autowired
    private WxMarketingRecommendPrizeService wxMarketingRecommendPrizeService;
    @Resource
    private OrderClient orderClient;
    @Resource
    private AllinHelper allinHelper;

    @RabbitListener(queues = OrderMqConstant.ORDER_PAY_SUCCESS_QUEUE)
    @RabbitHandler
    public void handleGroupOrder(byte[] receive) {
        String msg = new String(receive, StandardCharsets.UTF_8);
        try {
            log.debug("接收到订单支付成功消息。msg={}", msg);
            PaySuccessNotifyDTO notifyDTO = JSON.parseObject(msg, PaySuccessNotifyDTO.class);
            if (notifyDTO.getOrderType().equals(OrderTypeEnum.BULK.getStatus())) {
                log.info("处理团购已售数量和团单信息");
                wxMarketingGrouponService.handleNewOrder(notifyDTO);
                log.info("处理团购已售数量和团单信息完毕");
            }
            // 支付成功，下发配送单到达达
            CateringOrdersDeliveryNoEntity entity = orderClient.addOrderToDada(notifyDTO.getOrderId(), 0);
        } catch (Exception e) {
            log.error("订单支付成功后，后续处理发生异常", e);
        }
    }

    @RabbitListener(queues = OrderMqConstant.ORDER_CHECK_QUEUE)
    @RabbitHandler
    public void handleOrderCheckMsg(Map<String, Object> map) {
        log.debug("接收到订单核销完成消息msg={}", map);
        try {
            Long userId = Long.valueOf(map.get("userId").toString());
            Boolean isFirstOrderWithMerchant = Boolean.valueOf(map.get("isFirstOrderWithMerchant").toString());
            if (isFirstOrderWithMerchant) {
                //处理首单推荐有奖活动 赠送优惠券、积分
                wxMarketingRecommendPrizeService.handleNewUserFirstOrder(userId);
                log.info("用户核销完成，发放优惠券处理完成");
            }
        } catch (Exception e) {
            log.error("订单核销完成后，处理首单推荐有奖活动发生异常", e);
        }


    }


}
