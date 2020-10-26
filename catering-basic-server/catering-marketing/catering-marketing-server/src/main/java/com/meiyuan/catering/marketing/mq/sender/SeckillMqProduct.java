package com.meiyuan.catering.marketing.mq.sender;

import com.alibaba.fastjson.JSON;
import com.meiyuan.catering.marketing.dto.seckill.SeckillMsgBodyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName SeckillMqProduct
 * @Description
 * @Author gz
 * @Date 2020/3/26 12:46
 * @Version 1.1
 */
@Slf4j
@Component
public class SeckillMqProduct {

    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 发送消息---入队
     * @param body
     */
    public void send(SeckillMsgBodyDTO body){
        String msg = JSON.toJSONString(body);
        log.info("发送秒杀消息msg={}",msg);

        log.info("发送成功");
    }




}
