package com.meiyuan.catering.order.mq.sender;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.order.dto.OrderSecKillMqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 功能描述:  订单超时未支付取消订单
 *
 * @author: XiJie Xie
 * @date: 2020/3/24 16:42
 */
@Slf4j
@Component
public class OrderSecKillMqSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送取消订单还原秒杀商品库存消息
     */

    public void sendSeckillMsg(List<OrderSecKillMqDTO> list) {
        list.forEach(obj -> {
            JSONObject json = new JSONObject();
            json.put("pay", obj.getPay());
            json.put("order", obj.getOrder());
            json.put("seckillGoodsId", obj.getGoodsId());
            json.put("userId", obj.getUserId());
            json.put("number", obj.getNumber());
            json.put("seckillEventId", obj.getSeckillEventId());
            String msg = json.toJSONString();
            rabbitTemplate.convertAndSend(MarketingMqConstant.MARKETING_EXCHANGE, MarketingMqConstant.MARKETING_SECKILL_STOCK_QUEUE, msg.getBytes(StandardCharsets.UTF_8));
        });
    }
}
