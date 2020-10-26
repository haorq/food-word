package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.core.constant.OrderGroupConstant;
import com.meiyuan.catering.core.dto.order.delivery.OrderDelivery;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.notify.NotifyService;
import com.meiyuan.catering.core.notify.NotifyType;
import com.meiyuan.catering.core.util.SpringContextUtils;
import com.meiyuan.catering.marketing.dto.groupon.GrouponFailMsgDTO;
import com.meiyuan.catering.marketing.service.user.CateringUserTicketService;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.entity.CateringOrdersDiscountsEntity;
import com.meiyuan.catering.order.enums.OrderDiscountTypeEnum;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.enums.OrderOperationEnum;
import com.meiyuan.catering.order.enums.OrderOperationTypeEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.order.service.CateringOrdersDeliveryService;
import com.meiyuan.catering.order.service.CateringOrdersDiscountsService;
import com.meiyuan.catering.order.service.CateringOrdersOperationService;
import com.meiyuan.catering.pay.enums.PayEnum;
import com.meiyuan.catering.pay.service.MyPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zengzhangni
 * @date 2020/4/2OrderTimeOutPushMsgReceive
 */
@Slf4j
@Component
public class GroupFailReceive {

    @Resource
    private OrderClient orderClient;
    @Resource
    private CateringOrdersOperationService ordersOperationService;
    @Resource
    private CateringOrdersDeliveryService ordersDeliveryService;
    @Resource
    private NotifyService notifyService;
    @Resource
    private CateringOrdersDiscountsService cateringOrdersDiscountsService;
    @Resource
    private CateringUserTicketService cateringUserTicketService;


    @RabbitListener(queues = MarketingMqConstant.GROUP_FAIL_QUEUE)
    private void groupFail(byte[] receive) {

        log.debug("团购失败 退款开始...");
        try {
            List<String> failPhones = Lists.newArrayList();
            String msg = new String(receive, StandardCharsets.UTF_8);
            log.info("接收到团购失败的消息，msg={}", msg);
            GrouponFailMsgDTO failMsg = JSON.parseObject(msg, GrouponFailMsgDTO.class);


            List<Order> orders = list(failMsg.getFailureOrderNumbers());
            orders.forEach(order -> {
                try {
                    PayEnum payEnum = PayEnum.parse(order.getPayWay());
                    MyPayService payService = SpringContextUtils.getBean(payEnum.getImpl(), MyPayService.class);
                    payService.asyncCancel(order);
                    OrderOffDTO dto = new OrderOffDTO();
                    dto.setOrderId(order.getId().toString());
                    if(failMsg.getIsInitiative()) {
                        dto.setOffReason(OrderGroupConstant.GROUP_OPERATE_FAIL_MSG);
                    } else {
                        dto.setOffReason(OrderGroupConstant.GROUP_AUTO_FAIL_MSG);
                    }

                    dto.setOffUserId(OrderOffTypeEnum.AUTO_OFF.getCode().longValue());
                    dto.setOffUserName(OrderOffTypeEnum.AUTO_OFF.getDesc());
                    dto.setOffUserPhone("1");
                    orderClient.groupFailCancelOrder(dto);
                    ordersOperationService.saveOperation(order, "拼团失败,取消订单", OrderOperationEnum.CANCELED, OrderOperationTypeEnum.SYSTEM);
                    ordersOperationService.saveOperation(order, "拼团失败,自动退款", OrderOperationEnum.REFUND, OrderOperationTypeEnum.SYSTEM);
                    OrderDelivery deliveryEntity = ordersDeliveryService.getByOrderId(order.getId());
                    if (deliveryEntity != null) {
                        failPhones.add(deliveryEntity.getConsigneePhone());
                        // 获取用户优惠卷信息，并还原优惠卷
                        LambdaQueryWrapper<CateringOrdersDiscountsEntity> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(CateringOrdersDiscountsEntity::getOrderId, order.getId());
                        queryWrapper.eq(CateringOrdersDiscountsEntity::getDiscountType, OrderDiscountTypeEnum.TICKET.getCode());
                        CateringOrdersDiscountsEntity one = this.cateringOrdersDiscountsService.getOne(queryWrapper);
                        if (one != null) {
                            cateringUserTicketService.returnTicket(one.getDiscountId());
                        }
                    }
                } catch (Exception e) {
                    log.debug("团购失败退款异常:订单id:{},errInfo:{}", order.getId(), e.getMessage());
                }
            });
            sendFailMsg(failPhones);
            log.debug("团购失败 退款结束...");
        } catch (Exception e) {
            log.error("团购失败退款异常.", e);
        }
    }

    /**
     * 发送拼团失败消息
     *
     * @param failPhones
     */
    private void sendFailMsg(List<String> failPhones) {
        if (failPhones != null && !failPhones.isEmpty()) {
            String phones = Joiner.on(",").join(failPhones);
            notifyService.notifySmsTemplate(phones, NotifyType.GROUP_ORDER_FAILURE_NOTIFY, null);
        }
    }


    private List<Order> list(List<String> orderNumberList) {

        return orderClient.getOrderListByOrderNumbers(orderNumberList);

    }


}
