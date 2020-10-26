package com.meiyuan.catering.order.mq.sender;

import com.google.gson.Gson;
import com.meiyuan.catering.core.constant.OrderMqConstant;
import com.meiyuan.catering.core.util.JsonUtil;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.digest.MD2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单核销，消息发送
 *
 * @author lh
 */
@Slf4j
@Component
public class OrderCheckMqSender {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param userId                   用户ID
     * @param isFirstOrderWithMerchant 是否品牌首单
     */
    public void sendOrderCheckMsg(Long userId, Boolean isFirstOrderWithMerchant) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("userId", userId);
        map.put("isFirstOrderWithMerchant", isFirstOrderWithMerchant);
        log.debug("订单核销发送消息：" + JsonUtil.toJson(map));
        rabbitTemplate.convertAndSend(
                OrderMqConstant.ORDER_CHECK_EXCHANGE,
                OrderMqConstant.ORDER_CHECK_KEY,
                map);
    }
}
