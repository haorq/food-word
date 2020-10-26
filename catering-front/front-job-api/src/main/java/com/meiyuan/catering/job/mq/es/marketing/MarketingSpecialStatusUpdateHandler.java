package com.meiyuan.catering.job.mq.es.marketing;

import com.alibaba.fastjson.JSONObject;
import com.meiyuan.catering.core.constant.MarketingMqConstant;
import com.meiyuan.catering.job.service.merketing.MqSpecialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author GongJunZheng
 * @date 2020/09/07 18:09
 * @description 营销特价商品活动状态改变的消息
 **/

@Slf4j
@Component
@RabbitListener(queues = MarketingMqConstant.MARKETING_SPECIAL_STATUS_UPDATE_QUEUE)
public class MarketingSpecialStatusUpdateHandler {

    @Autowired
    private MqSpecialService mqSpecialService;

    @RabbitHandler
    public void process(byte[] msg) {
        this.process(new String(msg, StandardCharsets.UTF_8));
    }

    @RabbitHandler
    public void process(String msg) {
        log.info("营销特价商品活动改变状态消息：{}", msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Long specialId = Long.valueOf(jsonObject.get("specialId").toString());
            Integer status = Integer.valueOf(jsonObject.get("status").toString());
            mqSpecialService.statusUpdate(specialId, status);
        }catch (Exception e) {
            log.error("营销特价商品活动改变状态失败，异常.", e);
        }
    }

}
