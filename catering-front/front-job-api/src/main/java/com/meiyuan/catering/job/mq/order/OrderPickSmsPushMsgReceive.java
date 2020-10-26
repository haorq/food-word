package com.meiyuan.catering.job.mq.order;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.DateTimeUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.OrderPickSmsMqDTO;
import com.meiyuan.catering.order.enums.OrderStatusEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 功能描述:  下单成功距自提1小时短信通知
 * @Author: zengzhangni
 */
@Slf4j
@Component
public class OrderPickSmsPushMsgReceive {

    @Resource
    private NotifyService notifyService;
    @Resource
    private OrderClient orderClient;
    @Resource
    private CateringOrdersDeliveryService deliveryService;

    @RabbitListener(queues = OrderMqConstant.ORDER_PICK_SMS_QUEUE)
    public void ticketReceive(byte[] recived) {
        try {
            log.debug("下单成功距自提1小时短信通知 开始");
            String msg = new String(recived, StandardCharsets.UTF_8);
            OrderPickSmsMqDTO smsMqDTO = JSONObject.parseObject(msg, OrderPickSmsMqDTO.class);

            Result<Order> orderById = orderClient.getOrderById(smsMqDTO.getOrderId());
            Order order = orderById.getData();
            OrderDelivery deliveryEntity = deliveryService.getByOrderId(order.getId());

            if (Objects.equals(order.getStatus(), OrderStatusEnum.WAIT_TAKEN.getValue())) {
                String code = smsMqDTO.getConsigneeCode();
                LocalDateTime estimateTime = smsMqDTO.getEstimateTime();
                LocalDateTime estimateEndTime = smsMqDTO.getEstimateEndTime();

                String beginTime = DateTimeUtil.getTime(estimateTime);
                String endTime = DateTimeUtil.getTime(estimateEndTime);

                String storeName = deliveryEntity.getStoreName();
                String[] params = new String[]{beginTime, endTime, code,storeName,String.valueOf(order.getId())};
                notifyService.notifySmsTemplate(smsMqDTO.getPhone(), NotifyType.SELF_PICKUP_ONE_HOUR_NOTIFY, params);

                log.debug("下单成功距自提1小时短信通知 结束");
            } else {
                log.debug("订单状态不是待自提 不发短信通知.");
            }
        } catch (Exception e) {
            log.error("下单成功距自提1小时短信通知消费异常.",e);
        }
    }


}
