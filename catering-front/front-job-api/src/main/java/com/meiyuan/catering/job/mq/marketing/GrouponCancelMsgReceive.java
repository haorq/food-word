package com.meiyuan.catering.job.mq.marketing;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.dto.base.GrouponMemberQuitDTO;
import com.meiyuan.catering.marketing.feign.MarketingPullNewClient;
import com.meiyuan.catering.marketing.service.CateringMarketingGroupOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author luohuan
 * @date 2020/4/2
 * 团购订单取消的消息监听
 **/
@Slf4j
@Component
public class GrouponCancelMsgReceive {
    @Autowired
    private CateringMarketingGroupOrderService groupOrderService;
    @Autowired
    private MarketingPullNewClient pullNewClient;

    @RabbitListener(queues = OrderMqConstant.ORDER_GROUP_CANCEL_QUEUE)
    @RabbitHandler
    public void grouponCancelMsg(byte[] receive) {
        String msg = new String(receive, StandardCharsets.UTF_8);
        log.info("接收到团购订单取消的消息，msg={}", msg);
        try {
            GrouponMemberQuitDTO memberQuitDTO = JSON.parseObject(msg, GrouponMemberQuitDTO.class);
            groupOrderService.memberQuit(memberQuitDTO);
        } catch (Exception e) {
            log.error("团购订单取消消息处理失败", e);
        }
    }
}
