package com.meiyuan.catering.job.mq.order;

import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.order.feign.OrderClient;
import com.meiyuan.catering.pay.util.PaySupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 功能描述:  订单完成超时不能申请售后消息——消费消息
 *
 * @author: XiJie Xie
 * @date: 2020/3/24 16:42
 */
@Slf4j
@Component
@RabbitListener(queues = OrderMqConstant.ORDER_AFTER_SALES_EXPIRE_QUEUE)
public class OrderAfterSalesPushMsgReceive {
    @Autowired
    private OrderClient orderClient;
    @Autowired
    private PaySupport paySupport;

    @RabbitHandler
    public void ticketReceive(byte[] recived) {
        this.orderTimeOutPushMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * @param msg 订单完成超时不能申请售后消息-订单id
     */
    @RabbitHandler
    public void orderTimeOutPushMsg(String msg) {
        try {
            //修改订单不能申请售后
            orderClient.orderTimeOutAfterSales(msg);

            //处理订单分账,补贴(异步处理),验证售后状态
            paySupport.asyncDisposeSplitBill(msg, true);

        } catch (Exception e) {
            log.error("订单完成超时不能申请售后延迟任务处理异常:msg={},error={}", msg, e);
        }
    }

}
