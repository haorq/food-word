package com.meiyuan.catering.user.mq;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.user.entity.CateringCartEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author yaoozu
 * @description rabbit 的mq消息生成者
 * @date 2020/3/2710:39
 * @since v1.0.0
 */
@Slf4j
@Component
public class RabbitProduct {
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendSeckillMsg(List<CateringCartEntity> list){
        list.forEach(cart -> {
            JSONObject json = new JSONObject();
            json.put("seckillGoodsId",cart.getGoodsId());
            json.put("userId",cart.getUserId());
            json.put("number",cart.getNumber());
            String msg = json.toJSONString();
            rabbitTemplate.convertAndSend(MarketingMqConstant.MARKETING_EXCHANGE,MarketingMqConstant.MARKETING_SECKILL_STOCK_QUEUE,msg.getBytes(StandardCharsets.UTF_8));
            log.debug("发送清空购物车恢复秒杀库存消息成功:msg={}",msg);
        });
    }

}
