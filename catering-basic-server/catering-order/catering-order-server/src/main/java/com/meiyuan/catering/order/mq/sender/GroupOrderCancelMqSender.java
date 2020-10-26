package com.meiyuan.catering.order.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author luohuan
 * @date 2020/4/2
 **/
@Slf4j
@Component
public class GroupOrderCancelMqSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送取消团购订单的消息
     *
     * @param memberQuitDTO
     */
    public void sendGroupOrderCancelMsg(GrouponMemberQuitDTO memberQuitDTO) {
        try {
            String msg = JSON.toJSONString(memberQuitDTO);
            rabbitTemplate.convertAndSend(OrderMqConstant.ORDER_GROUP_CANCEL_EXCHANGE,
                    OrderMqConstant.ORDER_GROUP_CANCEL_KEY,
                    msg.getBytes());
            log.info("取消团购订单消息发送成功，msg={}", msg);
        } catch (Exception e) {
            log.error("取消团购订单消息发送失败", e);
        }
    }
}
