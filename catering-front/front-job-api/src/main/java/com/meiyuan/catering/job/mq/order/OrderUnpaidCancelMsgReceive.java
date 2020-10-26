package com.meiyuan.catering.job.mq.order;

import com.alibaba.fastjson.JSONArray;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.util.Result;
import com.meiyuan.catering.order.dto.OrderOffDTO;
import com.meiyuan.catering.order.enums.OrderOffTypeEnum;
import com.meiyuan.catering.order.feign.OrderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 功能描述:  待支付取消订单——消费消息
 *
 * @author: XiJie Xie
 * @date: 2020/3/24 16:42
 */
@Slf4j
@Component
@RabbitListener(queues = OrderMqConstant.UNPAID_CANCEL_QUEUE)
public class OrderUnpaidCancelMsgReceive {
    @Autowired
    private OrderClient orderClient;

    @RabbitHandler
    public void ticketReceive(byte[] recived) {
        this.orderUnpaidCancelMsg(new String(recived, StandardCharsets.UTF_8));
    }

    /**
     * @param msg 待支付取消订单-订单id
     */
    @RabbitHandler
    public void orderUnpaidCancelMsg(String msg) {
        try {
            List<String> orderIdList =  JSONArray.parseArray(msg, String.class);
            orderIdList.forEach(obj ->{
                OrderOffDTO dto = new OrderOffDTO();
                dto.setOrderId(obj);
                dto.setOffType(OrderOffTypeEnum.AUTO_OFF.getCode());
                dto.setOffReason("您的订单已取消");
                dto.setOffUserId(OrderOffTypeEnum.AUTO_OFF.getCode().longValue());
                dto.setOffUserName(OrderOffTypeEnum.AUTO_OFF.getDesc());
                dto.setOffUserPhone("1");
                Result<String> stringResult = orderClient.orderCancel(dto);
                log.info("待支付取消订单结果消息：【{}】", stringResult);
            });
        } catch (Exception e) {
            log.error("待支付取消订单处理异常:msg={},error={}", msg, e);
        }
    }
}
