package com.meiyuan.catering.job.mq.order;

import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.dto.pay.Order;
import com.meiyuan.catering.core.enums.base.PayWayEnum;
import com.meiyuan.catering.core.util.CacheLockUtil;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.enums.OrderStatusEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.pay.service.MyPayService;
import com.meiyuan.catering.pay.util.PayContext;
import com.meiyuan.catering.pay.util.PayLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * 功能描述:  订单超时未支付取消订单——消费消息
 *
 * @author: XiJie Xie
 * @date: 2020/3/24 16:42
 */
@Slf4j
@Component
@RabbitListener(queues = OrderMqConstant.ORDER_TIME_OUT_EXPIRE_QUEUE)
public class OrderTimeOutPushMsgReceive {
    @Autowired
    private OrderClient orderClient;
    @Resource
    private PayLock lock;

    @RabbitHandler
    public void ticketReceive(byte[] recived) {
        this.orderTimeOutPushMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * @param msg 订单超时未支付消息-订单id
     */
    @RabbitHandler
    public void orderTimeOutPushMsg(String msg) {
        try {

            log.debug("收到订单延迟支付自动取消任务消息：" + msg);

            Result<Order> orderById = orderClient.getOrderById(Long.valueOf(msg));
            if (orderById.success() && orderById.getData() != null) {
                //和支付回调用同一把锁
                lock.payLock(CacheLockUtil.orderPayNotifyLock(orderById.getData().getOrderNumber()), () -> {
                    Result<Order> orderResult = orderClient.getOrderById(Long.valueOf(msg));
                    if (orderResult.success() && orderResult.getData() != null) {
                        Order data = orderResult.getData();
                        if (OrderStatusEnum.UNPAID.getValue().equals(data.getStatus())) {
                            OrderOffDTO dto = new OrderOffDTO();
                            dto.setOffType(OrderOffTypeEnum.AUTO_OFF.getCode());
                            dto.setOrderId(msg);
                            dto.setOffReason("支付超时，订单已取消");
                            dto.setOffUserId(1L);
                            dto.setOffUserName(OrderOffTypeEnum.AUTO_OFF.getDesc());
                            dto.setOffUserPhone("1");
                            Result<String> stringResult = orderClient.orderCancel(dto);
                            log.info("订单超时未支付取消订单结果消息：【{}】", stringResult);
                            //微信订单关闭
                            closeOrder(data);
                        } else {
                            log.info("{}:订单状态不是待支付,取消订单忽略", data.getOrderNumber());
                        }
                    }
                });
            }
        } catch (Exception e) {
            log.error("订单超时未支付取消订单延迟任务处理异常:msg={},error={}", msg, e);
        }
    }

    /**
     * 如果用户微信支付了订单,但是系统处理数据失败,未修改订单状态
     * 在关闭取消订单时,判断是否微信支付,如果支付了则退款
     */
    private void closeOrder(Order order) {
        if (StringUtils.isNotBlank(order.getTradingFlow())) {
            try {
                MyPayService payService = PayContext.getPayService(PayWayEnum.WX_ALLIN);
                payService.closeOrder(order);
            } catch (Exception e) {
                log.error("微信关闭订单异常:{}", e);
            }
        } else {
            log.error("{}:订单未调用微信支付,微信关闭忽略..", order.getId());
        }

    }
}
